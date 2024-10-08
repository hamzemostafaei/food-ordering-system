package com.food.ordering.system.payment.service.data.access.creditentry.repository;

import com.food.ordering.system.payment.service.data.access.creditentry.entity.CreditEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ICreditEntryJpaRepository extends JpaRepository<CreditEntryEntity, String> {

    Optional<CreditEntryEntity> findByCustomerId(String customerId);


}
