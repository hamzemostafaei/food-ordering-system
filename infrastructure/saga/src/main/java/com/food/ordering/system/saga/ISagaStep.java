package com.food.ordering.system.saga;

import com.food.ordering.system.domain.event.IDomainEvent;

public interface ISagaStep<T, S extends IDomainEvent<?>, U extends IDomainEvent<?>> {

    S process(T data);

    U rollback(T data);

}
