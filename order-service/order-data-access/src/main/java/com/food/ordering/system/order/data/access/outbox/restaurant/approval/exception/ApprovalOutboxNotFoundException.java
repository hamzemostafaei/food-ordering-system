package com.food.ordering.system.order.data.access.outbox.restaurant.approval.exception;

public class ApprovalOutboxNotFoundException extends RuntimeException {
    public ApprovalOutboxNotFoundException(String message) {
        super(message);
    }
}
