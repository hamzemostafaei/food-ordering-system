package com.food.ordering.system.order.data.access.outbox.payment.exception;

public class PaymentOutboxNotFoundException extends RuntimeException {
  public PaymentOutboxNotFoundException(String message) {
    super(message);
  }
}
