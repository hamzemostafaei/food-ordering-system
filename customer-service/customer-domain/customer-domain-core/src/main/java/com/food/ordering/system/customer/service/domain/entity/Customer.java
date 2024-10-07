package com.food.ordering.system.customer.service.domain.entity;

import com.food.ordering.system.domain.entity.ABaseAggregateRoot;
import com.food.ordering.system.domain.value.object.CustomerId;

public class Customer extends ABaseAggregateRoot<CustomerId> {

    private final String username;
    private final String firstName;
    private final String lastName;

    public Customer(CustomerId customerId, String username, String firstName, String lastName) {
        super.setId(customerId);
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
