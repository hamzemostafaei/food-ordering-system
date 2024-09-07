package com.food.ordering.system.restaurant.service.domain.entity;

import com.food.ordering.system.domain.entity.ABAseEntity;
import com.food.ordering.system.domain.value.object.Money;
import com.food.ordering.system.domain.value.object.OrderId;
import com.food.ordering.system.domain.value.object.OrderStatus;

import java.util.List;

public class OrderDetail extends ABAseEntity<OrderId> {

    private final List<Product> products;
    private final OrderStatus orderStatus;
    private final Money totalAmount;

    private OrderDetail(Builder builder) {
        setId(builder.orderId);
        orderStatus = builder.orderStatus;
        totalAmount = builder.totalAmount;
        products = builder.products;
    }

    public static Builder builder() {
        return new Builder();
    }


    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    public List<Product> getProducts() {
        return products;
    }

    public static final class Builder {
        private OrderId orderId;
        private OrderStatus orderStatus;
        private Money totalAmount;
        private List<Product> products;

        private Builder() {
        }

        public Builder orderId(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder totalAmount(Money val) {
            totalAmount = val;
            return this;
        }

        public Builder products(List<Product> val) {
            products = val;
            return this;
        }

        public OrderDetail build() {
            return new OrderDetail(this);
        }
    }
}
