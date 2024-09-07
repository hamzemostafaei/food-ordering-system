package com.food.ordering.system.domain.value.object;

public enum OrderApprovalStatus {
    Approved(0),
    Rejected(1);

    private final int statusCode;

    OrderApprovalStatus(int statusCode) {
        this.statusCode = statusCode;
    }
}
