package com.food.ordering.system.data.access.restaurant.entity;

import jakarta.persistence.*;
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
@IdClass(RestaurantEntityId.class)
@Table(name = "order_restaurant_m_view", schema = "fos_restaurant")
public class RestaurantEntity {
    @Id
    @Column(length = 64)
    private String restaurantId;

    @Id
    @Column(length = 64)
    private String productId;

    @Column(length = 64)
    private String restaurantName;

    @Column
    private Boolean restaurantActive;

    @Column(length = 64)
    private String productName;

    @Column(precision = 22, scale = 2)
    private BigDecimal productPrice;

    @Column
    private Boolean productAvailable;

    public BigDecimal getProductPrice() {
        return productPrice.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantEntity that = (RestaurantEntity) o;
        return restaurantId.equals(that.restaurantId) && productId.equals(that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restaurantId, productId);
    }
}
