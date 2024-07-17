package com.food.ordering.system.domain.value.object;

import java.util.UUID;

public class CustomerId extends ABaseId<UUID> {
    public CustomerId(UUID value) {
        super(value);
    }
}
