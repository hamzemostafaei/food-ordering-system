package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.event.publisher.IDomainEventPublisher;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;

import java.util.List;

public interface IOrderDomainService {

    OrderCreatedEvent validateAndInitiateOrder(Order order,
                                               Restaurant restaurant,
                                               IDomainEventPublisher<OrderCreatedEvent> orderCreatedEventPublisher);

    OrderPaidEvent payOrder(Order order,IDomainEventPublisher<OrderPaidEvent> orderPaidEventPublisher);

    void approveOrder(Order order);

    OrderCancelledEvent cancelOrderPayment(Order order,
                                           List<String> failureMessages,
                                           IDomainEventPublisher<OrderCancelledEvent> orderCancelledEventPublisher);

    void cancelOrder(Order order, List<String> failureMessages);
}
