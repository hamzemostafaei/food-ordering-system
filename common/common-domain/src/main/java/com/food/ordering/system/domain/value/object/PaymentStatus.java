package com.food.ordering.system.domain.value.object;

public enum PaymentStatus {
    Completed(0),
    Cancelled(1),
    Failed(2);

    private final int statusCode;

    PaymentStatus(int statusCode) {
        this.statusCode = statusCode;
    }
}
