package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.event.EmptyEvent;
import com.food.ordering.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment.IOrderCanceledPaymentRequestMessagePublisher;
import com.food.ordering.system.order.service.domain.ports.output.repository.IOrderRepository;
import com.food.ordering.system.saga.ISagaStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderApprovalSaga implements ISagaStep<RestaurantApprovalResponse, EmptyEvent, OrderCancelledEvent> {

    private final IOrderDomainService orderDomainService;
    private final IOrderRepository orderRepository;
    private final IOrderCanceledPaymentRequestMessagePublisher orderCanceledPaymentRequestMessagePublisher;
    private final OrderSagaHelper orderSagaHelper;

    @Override
    @Transactional
    public EmptyEvent process(RestaurantApprovalResponse restaurantApprovalResponse) {
        log.info("Approving order with id: {}", restaurantApprovalResponse.getOrderId());
        Order order = orderSagaHelper.findOrder(restaurantApprovalResponse.getOrderId());
        orderDomainService.approveOrder(order);
        orderSagaHelper.saveOrder(order);
        log.info("Approved order with id: {}", restaurantApprovalResponse.getOrderId());
        return EmptyEvent.INSTANCE;
    }

    @Override
    @Transactional
    public OrderCancelledEvent rollback(RestaurantApprovalResponse restaurantApprovalResponse) {
        log.info("Cancelling order with id: {}", restaurantApprovalResponse.getOrderId());
        Order order = orderSagaHelper.findOrder(restaurantApprovalResponse.getOrderId());
        OrderCancelledEvent orderCancelledEvent =
                orderDomainService.cancelOrderPayment(order, restaurantApprovalResponse.getFailureMessages(), orderCanceledPaymentRequestMessagePublisher);
        orderSagaHelper.saveOrder(order);
        log.info("Canceled order with id: {}", restaurantApprovalResponse.getOrderId());
        return orderCancelledEvent;
    }
}
