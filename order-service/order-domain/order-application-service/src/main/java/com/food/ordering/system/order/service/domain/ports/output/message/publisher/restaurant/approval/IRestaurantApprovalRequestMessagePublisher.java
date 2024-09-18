package com.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurant.approval;

import com.food.ordering.system.order.service.domain.oubox.model.approval.OrderApprovalOutboxMessage;
import com.food.ordering.system.outbox.OutboxStatus;

import java.util.function.BiConsumer;

public interface IRestaurantApprovalRequestMessagePublisher {
    void publish(OrderApprovalOutboxMessage orderApprovalOutboxMessage, BiConsumer<OrderApprovalOutboxMessage, OutboxStatus> outboxCallback);
}
