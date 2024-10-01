package com.food.ordering.system.restaurant.service.data.access.restaurant.outbox.repository;

import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.restaurant.service.data.access.restaurant.outbox.entity.OrderOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderOutboxJpaRepository extends JpaRepository<OrderOutboxEntity, String> {

    Optional<List<OrderOutboxEntity>> findByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus);

    Optional<OrderOutboxEntity> findByTypeAndSagaIdAndOutboxStatus(String type, String sagaId, OutboxStatus outboxStatus);

    void deleteByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus);

}