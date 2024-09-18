package com.food.ordering.system.order.data.access.outbox.payment.entity;


import com.food.ordering.system.domain.value.object.OrderStatus;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.saga.SagaStatus;
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
@Table(name = "payment_outbox")
public class PaymentOutboxEntity {

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
    private SagaStatus sagaStatus;

    @Column
    private OrderStatus orderStatus;

    @Column
    private OutboxStatus outboxStatus;

    @Column
    @Version
    private int version;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentOutboxEntity that = (PaymentOutboxEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}