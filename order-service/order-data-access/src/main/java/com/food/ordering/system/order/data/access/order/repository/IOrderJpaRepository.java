package com.food.ordering.system.order.data.access.order.repository;

import com.food.ordering.system.order.data.access.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IOrderJpaRepository extends JpaRepository<OrderEntity, String> {
    Optional<OrderEntity> findByTrackingId(String trackingId);
}
