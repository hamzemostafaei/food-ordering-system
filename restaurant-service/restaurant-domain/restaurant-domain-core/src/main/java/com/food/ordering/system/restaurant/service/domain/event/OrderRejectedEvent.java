package com.food.ordering.system.restaurant.service.domain.event;

import com.food.ordering.system.domain.value.object.RestaurantId;
import com.food.ordering.system.restaurant.service.domain.entity.OrderApproval;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderRejectedEvent extends ABaseOrderApprovalEvent {

    public OrderRejectedEvent(OrderApproval orderApproval,
                              RestaurantId restaurantId,
                              List<String> failureMessages,
                              ZonedDateTime createdAt) {

        super(orderApproval, restaurantId, failureMessages, createdAt);
    }
}