package com.food.ordering.system.order.data.access.customer.repository;

import com.food.ordering.system.order.data.access.customer.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ICustomerJpaRepository extends JpaRepository<CustomerEntity, UUID> {
}
