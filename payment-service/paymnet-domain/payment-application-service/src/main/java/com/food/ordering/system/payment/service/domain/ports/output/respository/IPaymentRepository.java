package com.food.ordering.system.payment.service.domain.ports.output.respository;

import com.food.ordering.system.payment.service.domain.entity.Payment;

import java.util.Optional;

public interface IPaymentRepository {
    Payment save(Payment payment);

    Optional<Payment> findByOrderId(String orderId);
}
