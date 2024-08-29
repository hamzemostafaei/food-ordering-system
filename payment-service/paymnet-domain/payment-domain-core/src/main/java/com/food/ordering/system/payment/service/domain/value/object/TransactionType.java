package com.food.ordering.system.payment.service.domain.value.object;

public enum TransactionType {
    Debit(0),
    Credit(1);

    private final int value;

    TransactionType(int value) {
        this.value = value;
    }
}
