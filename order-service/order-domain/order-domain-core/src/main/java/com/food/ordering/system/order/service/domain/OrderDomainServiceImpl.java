package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.event.publisher.IDomainEventPublisher;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static com.food.ordering.system.domain.DomainConstants.UTC;

@Slf4j
public class OrderDomainServiceImpl implements IOrderDomainService {
    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order,
                                                      Restaurant restaurant,
                                                      IDomainEventPublisher<OrderCreatedEvent> orderCreatedEventPublisher) {
        validateRestaurant(restaurant);
        setProductOrderInformation(order, restaurant);
        order.validateOrder();
        order.initializeOrder();
        return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(UTC)), orderCreatedEventPublisher);
    }

    @Override
    public OrderPaidEvent payOrder(Order order, IDomainEventPublisher<OrderPaidEvent> orderPaidEventPublisher) {
        order.pay();
        log.info("Order with id: [{}] is paid", order.getId().getValue());
        return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of(UTC)), orderPaidEventPublisher);
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
        log.info("Order with Id: [{}] is approved", order.getId().getValue());
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order,
                                                  List<String> failureMessages,
                                                  IDomainEventPublisher<OrderCancelledEvent> orderCancelledEventPublisher) {
        order.initCancel(failureMessages);
        return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneId.of(UTC)), orderCancelledEventPublisher);
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);
    }

    private void validateRestaurant(Restaurant restaurant) {
        if (!restaurant.isActive()) {
            throw new OrderDomainException(
                    String.format("Restaurant with id %s is currently not active!", restaurant.getId())
            );
        }
    }

    private void setProductOrderInformation(Order order, Restaurant restaurant) {
        order
                .getItems()
                .forEach((orderItem) -> restaurant.getProducts()
                        .forEach((restaurantProduct) -> {
                            Product currentProduct = orderItem.getProduct();
                            if (currentProduct.equals(restaurantProduct)) {
                                currentProduct.updateWithConfirmNameAndPrice(
                                        restaurantProduct.getName(),
                                        restaurantProduct.getPrice()
                                );
                            }
                        }));
    }
}
