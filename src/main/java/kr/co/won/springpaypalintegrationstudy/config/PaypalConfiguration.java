package kr.co.won.springpaypalintegrationstudy.config;

import com.paypal.base.rest.APIContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * File Name        : PaypalConfiguration<br/>
 * COMMENT          :
 * Paypal SDK Configuration Class
 * </p>
 *
 * @author : Doukhee Won
 * @version : v0.0.1
 * @since : 2024. 5. 8.
 */
@Configuration
public class PaypalConfiguration {

    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.client-secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode;

    /**
     * @return
     */
    @Bean
    public APIContext apiContext() {
        return new APIContext(clientId, clientSecret, mode);
    }
}
