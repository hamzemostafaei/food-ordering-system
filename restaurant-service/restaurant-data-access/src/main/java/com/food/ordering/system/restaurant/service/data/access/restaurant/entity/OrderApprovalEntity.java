package com.food.ordering.system.restaurant.service.data.access.restaurant.entity;

import com.food.ordering.system.domain.value.object.OrderApprovalStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_approval", schema = "restaurant")
public class OrderApprovalEntity {
    @Id
    private String id;
    private String restaurantId;
    private String orderId;
    private OrderApprovalStatus status;
}
