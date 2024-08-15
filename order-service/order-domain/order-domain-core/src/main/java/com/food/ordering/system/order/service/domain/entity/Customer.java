package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.ABaseAggregateRoot;
import com.food.ordering.system.domain.value.object.CustomerId;

public class Customer extends ABaseAggregateRoot<CustomerId> {

    public Customer() {
    }

    public Customer(CustomerId customerId) {
        super.setId(customerId);
    }
}
