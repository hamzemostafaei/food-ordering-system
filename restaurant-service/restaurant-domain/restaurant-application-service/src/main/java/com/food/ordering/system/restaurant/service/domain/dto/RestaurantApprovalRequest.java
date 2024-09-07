package com.food.ordering.system.restaurant.service.domain.dto;

import com.food.ordering.system.domain.value.object.RestaurantOrderStatus;
import com.food.ordering.system.restaurant.service.domain.entity.Product;

public class RestaurantApprovalRequest {
    private String id;
    private String sagaId;
    private String restaurantId;
    private String orderId;
    private RestaurantOrderStatus restaurantOrderStatus;
    private java.util.List<Product> products;
    private java.math.BigDecimal price;
    private java.time.Instant createdAt;
}
