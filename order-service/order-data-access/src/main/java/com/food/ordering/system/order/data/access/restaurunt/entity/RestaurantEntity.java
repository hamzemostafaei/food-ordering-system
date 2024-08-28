package com.food.ordering.system.order.data.access.restaurunt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_restaurant_m_view", schema = "fos_restaurant")
public class RestaurantEntity {

    @EmbeddedId
    private RestaurantEntityId id;

    @Column(length = 64)
    private String restaurantName;

    @Column
    private Boolean restaurantActive;

    @Column(length = 64)
    private String productName;

    @Column(precision = 22, scale = 2)
    private BigDecimal productPrice;

    public BigDecimal getProductPrice() {
        return productPrice.setScale(2, RoundingMode.HALF_UP);
    }
}
