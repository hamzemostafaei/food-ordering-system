package com.food.ordering.system.customer.service;

import com.food.ordering.system.customer.service.domain.CustomerDomainServiceImpl;
import com.food.ordering.system.customer.service.domain.ICustomerDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public ICustomerDomainService customerDomainService() {
        return new CustomerDomainServiceImpl();
    }
}
