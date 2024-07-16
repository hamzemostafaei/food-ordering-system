package com.hamze.common.domain.value.object;

public enum OrderStatus {

    Pending(0),
    Paid(1),
    Approved(2),
    Cancelling(3),
    Cancelled(4);

    private final int statusCode;

    OrderStatus(int statusCode) {
        this.statusCode = statusCode;
    }


}
