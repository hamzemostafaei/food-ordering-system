package com.food.ordering.system.restaurant.service.domain;

import com.food.ordering.system.domain.event.publisher.IDomainEventPublisher;
import com.food.ordering.system.restaurant.service.domain.entity.Restaurant;
import com.food.ordering.system.restaurant.service.domain.event.ABaseOrderApprovalEvent;
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovedEvent;
import com.food.ordering.system.restaurant.service.domain.event.OrderRejectedEvent;

import java.util.List;

public interface IRestaurantDomainService {
    ABaseOrderApprovalEvent validateOrder(Restaurant restaurant,
                                          List<String> failureMessages,
                                          IDomainEventPublisher<OrderApprovedEvent> orderApprovedEventPublisher,
                                          IDomainEventPublisher<OrderRejectedEvent> orderRejectedEventPublisher);
}
