package com.food.ordering.system.domain.value.object;

public enum OrderApprovalStatus {
    Completed(0),
    Cancelled(1),
    Failed(2);

    private final int statusCode;

    OrderApprovalStatus(int statusCode) {
        this.statusCode = statusCode;
    }
}
