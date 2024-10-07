package com.food.ordering.system.order.data.access.customer.entity;

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
@Table(name = "customers")
public class CustomerEntity {
    @Id
    private String id;
    private String username;
    private String firstName;
    private String lastName;
}
