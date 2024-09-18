package com.food.ordering.system.saga;

public interface ISagaStep<T> {

    void process(T data);

    void rollback(T data);

}
