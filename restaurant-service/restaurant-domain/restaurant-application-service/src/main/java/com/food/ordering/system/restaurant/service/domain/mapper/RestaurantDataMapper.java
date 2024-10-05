package com.food.ordering.system.restaurant.service.domain.mapper;

import com.food.ordering.system.domain.value.object.Money;
import com.food.ordering.system.domain.value.object.OrderId;
import com.food.ordering.system.domain.value.object.OrderStatus;
import com.food.ordering.system.domain.value.object.RestaurantId;
import com.food.ordering.system.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.food.ordering.system.restaurant.service.domain.entity.OrderDetail;
import com.food.ordering.system.restaurant.service.domain.entity.Product;
import com.food.ordering.system.restaurant.service.domain.entity.Restaurant;
import com.food.ordering.system.restaurant.service.domain.event.ABaseOrderApprovalEvent;
import com.food.ordering.system.restaurant.service.domain.outbox.model.OrderEventPayload;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RestaurantDataMapper {

    public Restaurant restaurantApprovalRequestToRestaurant(RestaurantApprovalRequest restaurantApprovalRequest) {
        return Restaurant.builder()
                .restaurantId(new RestaurantId(restaurantApprovalRequest.getRestaurantId()))
                .orderDetail(OrderDetail.builder()
                        .orderId(new OrderId(restaurantApprovalRequest.getOrderId()))
                        .products(restaurantApprovalRequest.getProducts().stream().map(
                                        product -> Product.builder()
                                                .productId(product.getId())
                                                .quantity(product.getQuantity())
                                                .build())
                                .collect(Collectors.toList()))
                        .totalAmount(new Money(restaurantApprovalRequest.getPrice()))
                        .orderStatus(OrderStatus.valueOf(restaurantApprovalRequest.getRestaurantOrderStatus().name()))
                        .build())
                .build();
    }

    public OrderEventPayload orderApprovalEventToOrderEventPayload(ABaseOrderApprovalEvent orderApprovalEvent) {
        return OrderEventPayload.builder()
                .orderId(orderApprovalEvent.getOrderApproval().getOrderId().getValue())
                .restaurantId(orderApprovalEvent.getRestaurantId().getValue())
                .orderApprovalStatus(orderApprovalEvent.getOrderApproval().getApprovalStatus().getName())
                .createdAt(orderApprovalEvent.getCreatedAt())
                .failureMessages(orderApprovalEvent.getFailureMessages())
                .build();
    }
}
