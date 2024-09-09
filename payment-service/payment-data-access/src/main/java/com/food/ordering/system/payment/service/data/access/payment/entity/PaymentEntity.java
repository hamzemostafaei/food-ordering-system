package com.food.ordering.system.payment.service.data.access.payment.entity;

import com.food.ordering.system.domain.value.object.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
public class PaymentEntity {

    @Id
    @Column(length = 64)
    private String id;

    @Column(length = 64)
    private String customerId;

    @Column(length = 64)
    private String orderId;

    @Column(precision = 22, scale = 2)
    private BigDecimal price;

    @Column
    private PaymentStatus status;

    @Column
    private ZonedDateTime createdAt;

    public BigDecimal getPrice() {
        return price.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentEntity that = (PaymentEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
