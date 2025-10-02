package com.iprody.xpayment.adapter.app.api;


import com.iprody.xpayment.adapter.app.api.*;
import com.iprody.xpayment.adapter.app.api.ApiClient;
import com.iprody.xpayment.adapter.app.api.client.DefaultApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
class XPaymentRestClientConfig {
    @Bean
    RestTemplate xpaymentRestTemplate(@Value("${x-payment-api.client.url}") String username,
                                      @Value("${x-payment-api.client.password}") String password,
                                      @Value("${x-payment-api.client.account}") String xPayAccount) {
        RestTemplate rt = new RestTemplate();
        rt.getInterceptors().add((req, body, ex) -> {
            req.getHeaders().setBasicAuth(username, password);
            req.getHeaders().add("X-Pay-Account", xPayAccount);
            return ex.execute(req, body);
        });
        return rt;
    }
    @Bean
    ApiClient xpaymentApiClient(@Value("${x-payment-api.client.url}") String xPaymentUrl,
            RestTemplate xpaymentRestTemplate
    ) {
        ApiClient apiClient = new ApiClient(xpaymentRestTemplate);
        apiClient.setBasePath(xPaymentUrl);
        return apiClient;
    }
    @Bean
    DefaultApi defaultApi(ApiClient apiClient) {
        return new DefaultApi(apiClient);
    }
}