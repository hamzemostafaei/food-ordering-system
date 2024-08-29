package com.food.ordering.system.domain.value.object;

public enum PaymentOrderStatus {

    Pending(0),
    Cancelled(1);

    private final int value;

    PaymentOrderStatus(int value) {
        this.value = value;
    }
}
