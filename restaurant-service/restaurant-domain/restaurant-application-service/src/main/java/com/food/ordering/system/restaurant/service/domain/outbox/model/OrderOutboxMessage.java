package com.food.ordering.system.restaurant.service.domain.outbox.model;

import com.food.ordering.system.domain.value.object.OrderApprovalStatus;
import com.food.ordering.system.outbox.OutboxStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Builder
@AllArgsConstructor
public class OrderOutboxMessage {

    private String id;
    private String sagaId;
    private ZonedDateTime createdAt;
    private ZonedDateTime processedAt;
    private String type;
    private String payload;
    @Setter
    private OutboxStatus outboxStatus;
    private OrderApprovalStatus approvalStatus;
    private int version;

}