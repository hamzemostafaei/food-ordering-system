package com.food.ordering.system.domain.value.object;

import java.util.UUID;

public class ProductId extends ABaseId<UUID>{
    public ProductId(UUID value) {
        super(value);
    }
}
