package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.ABaseAggregateRoot;
import com.food.ordering.system.domain.value.object.RestaurantId;

import java.util.List;

public class Restaurant extends ABaseAggregateRoot<RestaurantId> {
    private final List<Product> products;
    private final boolean active;

    private Restaurant(Builder builder) {
        super.setId(builder.restaurantId);
        products = builder.products;
        active = builder.active;
    }


    public List<Product> getProducts() {
        return products;
    }

    public boolean isActive() {
        return active;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private RestaurantId restaurantId;
        private List<Product> products;
        private boolean active;

        private Builder() {
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder products(List<Product> val) {
            products = val;
            return this;
        }

        public Builder active(boolean val) {
            active = val;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "products=" + products +
                ", active=" + active +
                '}';
    }
}
