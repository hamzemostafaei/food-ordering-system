package com.food.ordering.system.payment.service.domain.ports.output.respository;

import com.food.ordering.system.domain.value.object.CustomerId;
import com.food.ordering.system.payment.service.domain.entity.CreditHistory;

import java.util.List;
import java.util.Optional;

public interface ICreditHistoryRepository {
    CreditHistory save(CreditHistory creditHistory);

    Optional<List<CreditHistory>> findByCustomerId(CustomerId customerId);
}
