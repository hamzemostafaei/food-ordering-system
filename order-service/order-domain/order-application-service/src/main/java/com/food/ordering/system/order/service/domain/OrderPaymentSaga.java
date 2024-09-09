package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.event.EmptyEvent;
import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurant.approval.IOrderPaidRestaurantRequestMessagePublisher;
import com.food.ordering.system.saga.ISagaStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPaymentSaga implements ISagaStep<PaymentResponse, OrderPaidEvent, EmptyEvent> {

    private final IOrderDomainService orderDomainService;
    private final IOrderPaidRestaurantRequestMessagePublisher orderPaidRestaurantRequestMessagePublisher;
    private final OrderSagaHelper orderSagaHelper;

    @Override
    @Transactional
    public OrderPaidEvent process(PaymentResponse paymentResponse) {
        log.info("Completing payment for order with id {}", paymentResponse.getOrderId());

        Order order = orderSagaHelper.findOrder(paymentResponse.getOrderId());
        OrderPaidEvent orderPaidEvent = orderDomainService.payOrder(order, orderPaidRestaurantRequestMessagePublisher);
        orderSagaHelper.saveOrder(order);
        log.info("Order with id {} has been paid successfully", paymentResponse.getOrderId());
        return orderPaidEvent;
    }

    @Override
    @Transactional
    public EmptyEvent rollback(PaymentResponse paymentResponse) {
        log.info("Cancelling order with id {}", paymentResponse.getOrderId());
        Order order = orderSagaHelper.findOrder(paymentResponse.getOrderId());
        orderDomainService.cancelOrder(order, paymentResponse.getFailureMessages());
        orderSagaHelper.saveOrder(order);
        log.info("Order with id {} has been cancelled", paymentResponse.getOrderId());
        return EmptyEvent.INSTANCE;
    }
}
