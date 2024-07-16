package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.ABAseEntity;
import com.food.ordering.system.domain.value.object.Money;
import com.food.ordering.system.domain.value.object.ProductId;

public class Product extends ABAseEntity<ProductId> {

    private String name;
    private Money price;

    public Product(ProductId productId, String name, Money price) {
        super.setId(productId);
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }
}
