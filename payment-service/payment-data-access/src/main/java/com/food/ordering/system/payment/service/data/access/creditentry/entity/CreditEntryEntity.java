package com.food.ordering.system.payment.service.data.access.creditentry.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "credit_entry")
public class CreditEntryEntity {

    @Id
    @Column(length = 64)
    private String id;

    @Column(length = 64)
    private String customerId;

    @Column(precision = 22, scale = 2)
    private BigDecimal totalCreditAmount;

    public BigDecimal getTotalCreditAmount() {
        return totalCreditAmount.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreditEntryEntity that = (CreditEntryEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
