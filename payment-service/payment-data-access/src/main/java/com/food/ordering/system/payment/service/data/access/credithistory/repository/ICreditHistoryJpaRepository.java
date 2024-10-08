package com.food.ordering.system.payment.service.data.access.credithistory.repository;

import com.food.ordering.system.payment.service.data.access.credithistory.entity.CreditHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICreditHistoryJpaRepository extends JpaRepository<CreditHistoryEntity, String> {

    Optional<List<CreditHistoryEntity>> findByCustomerId(String customerId);


}
