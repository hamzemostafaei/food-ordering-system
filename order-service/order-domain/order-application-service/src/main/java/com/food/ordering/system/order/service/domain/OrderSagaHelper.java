package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.value.object.OrderId;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.exception.OrderNotFoundException;
import com.food.ordering.system.order.service.domain.ports.output.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderSagaHelper {
    private final IOrderRepository orderRepository;

    Order findOrder(String orderId) {
        Optional<Order> orderResponse = orderRepository.findById(new OrderId(orderId));
        if (orderResponse.isEmpty()) {
            log.error("Order with id: [{}] could not be found!", orderId);
            throw new OrderNotFoundException(String.format("Order with id: [%s] could not be found!", orderId));
        }
        return orderResponse.get();
    }

    void saveOrder(Order order) {
        orderRepository.save(order);
    }
}
