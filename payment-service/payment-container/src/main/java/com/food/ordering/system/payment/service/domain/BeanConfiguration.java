package com.food.ordering.system.payment.service.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Bean
    public IPaymentDomainService paymentDomainService() {
        return new PaymentDomainServiceImpl();
    }
}
