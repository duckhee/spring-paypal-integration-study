package kr.co.won.springpaypalintegrationstudy.paypal;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

/**
 * <p>
 * File Name        : PaypalController<br/>
 * COMMENT          :
 * Paypal using controller
 * </p>
 *
 * @author : Doukhee Won
 * @version : v0.0.1
 * @since : 2024. 5. 8.
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class PaypalController {

    private final PaypalService paypalService;

    @GetMapping
    public String paypalHome() {
        return "paypal/index";
    }

    @PostMapping(path = "/payment/create")
    public RedirectView createPaymentDo() {
        try {
            String cancelUrl = "https://localhost:8080/payment/cancel";
            String successUrl = "https://localhost:8080/payment/success";
            Payment payment = paypalService.createPayment(10.0, "USD", "paypal", "sale", "Payment Description", cancelUrl, successUrl);

            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approve_url")) {
                    return new RedirectView(link.getHref());
                }
            }
        } catch (PayPalRESTException payPalRESTException) {
            log.error("Error occurred : {}", payPalRESTException);
        }
        return new RedirectView("/payment/error");
    }

    @GetMapping(path = "/payment/success")
    public String paymentSuccess(@RequestParam(name = "paymentId") String paymentId, @RequestParam(name = "PayerID") String payerId) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                return "paypal/payment-success";
            }
        } catch (PayPalRESTException payPalRESTException) {
            log.error("Error occurred : {}", payPalRESTException);
        }
        return "paypal/payment-success";
    }

    @GetMapping(path = "/payment/cancel")
    public String paymentCancel() {
        return "paypal/payment-cancel";
    }

    @GetMapping(path = "/payment/error")
    public String paymentError() {
        return "paypal/payment-error";
    }
}
