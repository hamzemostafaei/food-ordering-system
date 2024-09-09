package com.food.ordering.system.order.data.access.order.entity;

import com.food.ordering.system.domain.value.object.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class OrderEntity {

    @Id
    @Column(length = 64)
    private String id;

    @Column(length = 64)
    private String customerId;

    @Column(length = 64)
    private String restaurantId;

    @Column(length = 64)
    private String trackingId;

    @Column(precision = 22, scale = 2)
    private BigDecimal price;

    @Column(length = 64)
    private OrderStatus orderStatus;

    @Column(length = 256)
    private String failureMessages;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private OrderAddressEntity address;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItemEntity> items;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntity that = (OrderEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
