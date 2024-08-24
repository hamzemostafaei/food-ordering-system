package com.food.ordering.system.order.data.access.restaurunt.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
//@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@Table(name = "order_restaurant_m_view", schema = "restaurant")
public class RestaurantEntity {

    @EmbeddedId
    private RestaurantEntityId id;
    private String restaurantName;
    private Boolean restaurantActive;
    private String productName;
    private BigDecimal productPrice;
}
