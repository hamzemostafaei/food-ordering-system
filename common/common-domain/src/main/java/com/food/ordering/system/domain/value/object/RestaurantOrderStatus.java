package com.food.ordering.system.domain.value.object;

public enum RestaurantOrderStatus {
    Paid(0);

    private final int statusCode;

    RestaurantOrderStatus(int statusCode) {
        this.statusCode = statusCode;
    }
}
