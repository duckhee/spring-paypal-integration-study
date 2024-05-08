package kr.co.won.springpaypalintegrationstudy.paypal;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * <p>
 * File Name        : PaypalService<br/>
 * COMMENT          :
 * Paypal pay Service
 * </p>
 *
 * @author : Doukhee Won
 * @version : v0.0.1
 * @since : 2024. 5. 8.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaypalService {

    /**
     * paypal 에서 제공하는 객체 -> 결제 정보를 억기 위해 접근을 하는 context 이다.
     */
    private final APIContext apiContext;

    /**
     * <p>
     * 결제에 대한 정보를 받아서 결쩨 진행을 위한 객체를 생성하는 함수
     * </p>
     *
     * @param total
     * @param currency
     * @param method
     * @param intent
     * @param description
     * @param cancelUrl
     * @param successUrl
     * @return
     * @throws PayPalRESTException
     */
    public Payment createPayment(Double total, String currency, String method, String intent, String description, String cancelUrl, String successUrl) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        /** Locale 정보에 따라 금액에 대해서 설정 하는 것 -> 환율 변환 한느 것 */
        amount.setTotal(String.format(Locale.forLanguageTag(currency), "%.2f", total));
        /** 결제 요청 정보 삽입 */
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(description);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        /** 결제자에 대한 정보를 넣어주는 것 */
        Payer payer = new Payer();
        payer.setPaymentMethod(method);
        /** 결제에 대한 정보를 담아 두는 것 */
        Payment payment = new Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        /** 결제 실패 및 성공 시 이동 시킬 주소를 넣어주기 위한 객체 */
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);

        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    /**
     * <p>
     * 결제에 대한 진행을 위한 함수이다.
     * </p>
     *
     * @param paymentId
     * @param payerId
     * @return
     * @throws PayPalRESTException
     */
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        /** 결제에 대한 정보를 설정 */
        Payment payment = new Payment();
        payment.setId(paymentId);
        /** 결제를 진행하기 위한 객체 생성 */
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        return payment.execute(apiContext, paymentExecution);
    }

}
