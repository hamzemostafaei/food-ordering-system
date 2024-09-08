package com.food.ordering.system.restaurant.service.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.food.ordering.system")
@EntityScan(basePackages = {"com.food.ordering.system.restaurant.service.data.access", "com.food.ordering.system.data.access"})
@EnableJpaRepositories(basePackages = {"com.food.ordering.system.restaurant.service.data.access", "com.food.ordering.system.data.access"})
public class RestaurantServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestaurantServiceApplication.class, args);
    }
}
