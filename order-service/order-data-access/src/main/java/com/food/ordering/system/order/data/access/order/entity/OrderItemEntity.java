package com.food.ordering.system.order.data.access.order.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_items")
@IdClass(OrderItemEntityId.class)
public class OrderItemEntity {
    @Id
    @Column(length = 19)
    private Long id;
    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order;

    @Column(length = 64)
    private String productId;

    @Column(length = 22,precision = 22,scale = 2)
    private BigDecimal price;

    @Column(precision = 10)
    private Integer quantity;

    @Column(precision = 22, scale = 2)
    private BigDecimal subTotal;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemEntity that = (OrderItemEntity) o;
        return id.equals(that.id) && order.equals(that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order);
    }
}
