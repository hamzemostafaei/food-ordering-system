package com.food.ordering.system.restaurant.service.data.access.restaurant.outbox.entity;


import com.food.ordering.system.domain.value.object.OrderApprovalStatus;
import com.food.ordering.system.domain.value.object.PaymentStatus;
import com.food.ordering.system.outbox.OutboxStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_outbox")
public class OrderOutboxEntity {

    @Id
    @Column(length = 64)
    private String id;

    @Column(length = 64)
    private String sagaId;

    @Column
    private ZonedDateTime createdAt;

    @Column
    private ZonedDateTime processedAt;

    @Column
    private String type;

    @Column
    private String payload;

    @Column
    private OutboxStatus outboxStatus;

    @Column
    private OrderApprovalStatus approvalStatus;

    @Column
    @Version
    private int version;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderOutboxEntity that = (OrderOutboxEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}