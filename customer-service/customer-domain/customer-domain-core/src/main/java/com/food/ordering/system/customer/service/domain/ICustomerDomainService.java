package com.food.ordering.system.customer.service.domain;

import com.food.ordering.system.customer.service.domain.entity.Customer;
import com.food.ordering.system.customer.service.domain.event.CustomerCreatedEvent;

public interface ICustomerDomainService {
    CustomerCreatedEvent validateAndInitiateCustomer(Customer customer);
}
