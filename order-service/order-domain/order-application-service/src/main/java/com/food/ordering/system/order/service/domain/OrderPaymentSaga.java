package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.event.EmptyEvent;
import com.food.ordering.system.domain.value.object.OrderId;
import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.exception.OrderNotFoundException;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurant.approval.IOrderPaidRestaurantRequestMessagePublisher;
import com.food.ordering.system.order.service.domain.ports.output.repository.IOrderRepository;
import com.food.ordering.system.saga.ISagaStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPaymentSaga implements ISagaStep<PaymentResponse, OrderPaidEvent, EmptyEvent> {

    private final IOrderDomainService orderDomainService;
    private final IOrderRepository orderRepository;
    private final IOrderPaidRestaurantRequestMessagePublisher orderPaidRestaurantRequestMessagePublisher;

    @Override
    public OrderPaidEvent process(PaymentResponse paymentResponse) {
        log.info("Completing payment for order with id {}", paymentResponse.getOrderId());

        Order order = findOrder(paymentResponse.getOrderId());
        OrderPaidEvent orderPaidEvent = orderDomainService.payOrder(order, orderPaidRestaurantRequestMessagePublisher);
        orderRepository.save(order);
        log.info("Order with id {} has been paid successfully", paymentResponse.getOrderId());
        return orderPaidEvent;
    }

    @Override
    public EmptyEvent rollback(PaymentResponse paymentResponse) {
        log.info("Cancelling order with id {}", paymentResponse.getOrderId());
        Order order = findOrder(paymentResponse.getOrderId());
        orderDomainService.cancelOrder(order, paymentResponse.getFailureMessages());
        orderRepository.save(order);
        log.info("Order with id {} has been cancelled", paymentResponse.getOrderId());
        return EmptyEvent.INSTANCE;
    }

    private Order findOrder(String orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(new OrderId(orderId));

        if (optionalOrder.isEmpty()) {
            log.error("Order with id {} not found", orderId);
            throw new OrderNotFoundException("Order with id " + orderId + " not found");
        }
        return optionalOrder.get();
    }
}
