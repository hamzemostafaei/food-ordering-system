package com.food.ordering.system.outbox;

public interface IOutboxScheduler {
    void processOutboxMessage();
}
