package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.ABaseAggregateRoot;
import com.food.ordering.system.domain.value.object.*;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.value.object.OrderItemId;
import com.food.ordering.system.order.service.domain.value.object.StreetAddress;
import com.food.ordering.system.order.service.domain.value.object.TrackingId;

import java.util.List;
import java.util.UUID;

public class Order extends ABaseAggregateRoot<OrderId> {
    public static final String FAILURE_MESSAGE_DELIMITER = ",";
    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress deliveryAddress;
    private final Money price;
    private final List<OrderItem> items;
    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    private Order(Builder builder) {
        super.setId(builder.orderId);
        customerId = builder.customerId;
        restaurantId = builder.restaurantId;
        deliveryAddress = builder.deliveryAddress;
        price = builder.price;
        items = builder.items;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
        failureMessages = builder.failureMessages;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void initializeOrder() {
        setId(new OrderId(UUID.randomUUID().toString()));
        trackingId = new TrackingId(UUID.randomUUID().toString());
        orderStatus = OrderStatus.Pending;
        initializeOrderItems();
    }

    public void validateOrder() {
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    public void pay() {
        if (orderStatus != OrderStatus.Pending) {
            throw new OrderDomainException("Order is not in correct state for pay operation!");
        }
        orderStatus = OrderStatus.Paid;
    }

    public void approve() {
        if (orderStatus != OrderStatus.Paid) {
            throw new OrderDomainException("Order is not in correct state for approve operation!");
        }
        orderStatus = OrderStatus.Approved;
    }

    public void initCancel(List<String> failureMessages) {
        if (orderStatus != OrderStatus.Paid) {
            throw new OrderDomainException("Order is not in correct state for init-cancel operation!");
        }

        orderStatus = OrderStatus.Cancelling;

        updateFailureMessages(failureMessages);
    }

    private void updateFailureMessages(List<String> failureMessages) {
        if (this.failureMessages != null && !this.failureMessages.isEmpty() && failureMessages != null) {
            this.failureMessages.addAll(
                    failureMessages
                            .stream()
                            .filter((message) -> !(message != null && message.isEmpty()))
                            .toList()
            );
        }

        if (this.failureMessages == null || this.failureMessages.isEmpty()) {
            this.failureMessages = failureMessages;
        }

    }

    public void cancel(List<String> failureMessages) {
        if (!(orderStatus == OrderStatus.Cancelling || orderStatus == OrderStatus.Pending)) {
            throw new OrderDomainException("Order is not in correct state for cancel operation!");
        }
        orderStatus = OrderStatus.Cancelled;
        updateFailureMessages(failureMessages);
    }

    private void validateItemsPrice() {
        Money orderItemsTotal = this.items.stream()
                .map((orderItem) -> {
                    validateItemPrice(orderItem);
                    return orderItem.getSubTotal();
                })
                .reduce(Money.ZERO, Money::add);

        if (!price.equals(orderItemsTotal)) {
            throw new OrderDomainException(
                    String.format(
                            "Total price: %s is not equal ot order items total: %s !",
                            price.getAmount(),
                            orderItemsTotal.getAmount()
                    )
            );
        }
    }

    private void validateItemPrice(OrderItem orderItem) {
        if (!orderItem.isPriceValid()) {
            throw new OrderDomainException(
                    String.format(
                            "Order item price: [%s] is not valid for product: [%s] ",
                            orderItem.getPrice().getAmount(),
                            orderItem.getProduct().getId().getValue()
                    )
            );
        }
    }

    private void validateTotalPrice() {
        if (price == null || !price.isGreaterThanZero()) {
            throw new OrderDomainException("Total price must be greater than zero!");
        }

    }

    private void validateInitialOrder() {
        if (orderStatus != null || getId() != null) {
            throw new OrderDomainException("Order is not in correct state for initialization!");
        }
    }

    private void initializeOrderItems() {
        long orderItemId = 1;
        for (OrderItem item : items) {
            item.initializeOrderItem(super.getId(), new OrderItemId(orderItemId++));
        }
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public StreetAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    @Override
    public String toString() {
        return "Order{" +
                "customerId=" + customerId +
                ", restaurantId=" + restaurantId +
                ", deliveryAddress=" + deliveryAddress +
                ", price=" + price +
                ", items=" + items +
                ", trackingId=" + trackingId +
                ", orderStatus=" + orderStatus +
                ", failureMessages=" + failureMessages +
                '}';
    }

    public static final class Builder {
        private OrderId orderId;
        private CustomerId customerId;
        private RestaurantId restaurantId;
        private StreetAddress deliveryAddress;
        private Money price;
        private List<OrderItem> items;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;

        private Builder() {
        }

        public Builder orderId(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder deliveryAddress(StreetAddress val) {
            deliveryAddress = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder items(List<OrderItem> val) {
            items = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
