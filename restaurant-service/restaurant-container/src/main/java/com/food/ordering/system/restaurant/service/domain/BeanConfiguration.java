package com.food.ordering.system.restaurant.service.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Bean
    public IRestaurantDomainService restaurantDomainService() {
        return new RestaurantDomainServiceImpl();
    }
}
