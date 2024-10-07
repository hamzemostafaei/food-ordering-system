package com.food.ordering.system.customer.service.domain.ports.output.message.publisher;

import com.food.ordering.system.customer.service.domain.event.CustomerCreatedEvent;

public interface ICustomerMessagePublisher {
    void publish(CustomerCreatedEvent customerCreatedEvent);
}
