package com.food.ordering.system.customer.service.domain.event;

import com.food.ordering.system.customer.service.domain.entity.Customer;
import com.food.ordering.system.domain.event.IDomainEvent;

import java.time.ZonedDateTime;

public class CustomerCreatedEvent implements IDomainEvent<Customer> {

    private final Customer customer;

    private final ZonedDateTime createdAt;

    public CustomerCreatedEvent(Customer customer, ZonedDateTime createdAt) {
        this.customer = customer;
        this.createdAt = createdAt;
    }

    public Customer getCustomer() {
        return customer;
    }
}
