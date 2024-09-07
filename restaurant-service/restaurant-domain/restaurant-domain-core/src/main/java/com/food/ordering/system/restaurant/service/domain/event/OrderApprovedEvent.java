package com.food.ordering.system.restaurant.service.domain.event;

import com.food.ordering.system.domain.event.publisher.IDomainEventPublisher;
import com.food.ordering.system.domain.value.object.RestaurantId;
import com.food.ordering.system.restaurant.service.domain.entity.OrderApproval;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderApprovedEvent extends ABaseOrderApprovalEvent {

    private final IDomainEventPublisher<OrderApprovedEvent> orderApprovedEventPublisher;

    public OrderApprovedEvent(OrderApproval orderApproval,
                              RestaurantId restaurantId,
                              List<String> failureMessages,
                              ZonedDateTime createdAt,
                              IDomainEventPublisher<OrderApprovedEvent> orderApprovedEventPublisher) {

        super(orderApproval, restaurantId, failureMessages, createdAt);
        this.orderApprovedEventPublisher = orderApprovedEventPublisher;
    }

    @Override
    public void fire() {
        this.orderApprovedEventPublisher.publish(this);
    }
}
