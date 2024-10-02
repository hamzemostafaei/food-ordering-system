package com.food.ordering.system.payment.service.domain.ports.output.respository;

import com.food.ordering.system.domain.value.object.CustomerId;
import com.food.ordering.system.payment.service.domain.entity.CreditEntry;

import java.util.Optional;

public interface ICreditEntryRepository {

    CreditEntry save(CreditEntry creditEntry);

    Optional<CreditEntry> findByCustomerId(CustomerId customerId);
}
