package com.food.ordering.system.restaurant.service.domain.event;

import com.food.ordering.system.domain.event.publisher.IDomainEventPublisher;
import com.food.ordering.system.domain.value.object.RestaurantId;
import com.food.ordering.system.restaurant.service.domain.entity.OrderApproval;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderRejectedEvent extends ABaseOrderApprovalEvent {

    private final IDomainEventPublisher<OrderRejectedEvent> orderRejectedEventPublisher;

    public OrderRejectedEvent(OrderApproval orderApproval,
                              RestaurantId restaurantId,
                              List<String> failureMessages,
                              ZonedDateTime createdAt,
                              IDomainEventPublisher<OrderRejectedEvent> orderApprovedEventPublisher) {

        super(orderApproval, restaurantId, failureMessages, createdAt);
        this.orderRejectedEventPublisher = orderApprovedEventPublisher;
    }

    @Override
    public void fire() {
        this.orderRejectedEventPublisher.publish(this);
    }
}