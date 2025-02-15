package com.food.ordering.system.payment.service.domain;

import com.food.ordering.system.domain.value.object.CustomerId;
import com.food.ordering.system.domain.value.object.PaymentStatus;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.entity.CreditEntry;
import com.food.ordering.system.payment.service.domain.entity.CreditHistory;
import com.food.ordering.system.payment.service.domain.entity.Payment;
import com.food.ordering.system.payment.service.domain.event.PaymentEvent;
import com.food.ordering.system.payment.service.domain.exception.PaymentApplicationServiceException;
import com.food.ordering.system.payment.service.domain.exception.PaymentNotFoundException;
import com.food.ordering.system.payment.service.domain.mapper.PaymentDataMapper;
import com.food.ordering.system.payment.service.domain.outbox.model.OrderOutboxMessage;
import com.food.ordering.system.payment.service.domain.outbox.scheduler.OrderOutboxHelper;
import com.food.ordering.system.payment.service.domain.ports.output.message.publisher.IPaymentResponseMessagePublisher;
import com.food.ordering.system.payment.service.domain.ports.output.respository.ICreditEntryRepository;
import com.food.ordering.system.payment.service.domain.ports.output.respository.ICreditHistoryRepository;
import com.food.ordering.system.payment.service.domain.ports.output.respository.IPaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class PaymentRequestHelper {

    private final IPaymentDomainService paymentDomainService;
    private final PaymentDataMapper paymentDataMapper;
    private final IPaymentRepository paymentRepository;
    private final ICreditEntryRepository creditEntryRepository;
    private final ICreditHistoryRepository creditHistoryRepository;
    private final OrderOutboxHelper orderOutboxHelper;
    private final IPaymentResponseMessagePublisher paymentResponseMessagePublisher;


    @Transactional
    public void persistPayment(PaymentRequest paymentRequest) {
        if (publishIfOutboxMessageProcessedForPayment(paymentRequest, PaymentStatus.Completed)) {
            if (log.isInfoEnabled()) {
                log.info("An outbox message with saga id: [{}] is already saved to database!", paymentRequest.getSagaId());
            }
            return;
        }

        if (log.isInfoEnabled()) {
            log.info("Received payment complete event for order id: [{}]", paymentRequest.getOrderId());
        }
        Payment payment = paymentDataMapper.paymentRequestModelToPayment(paymentRequest);
        CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        List<CreditHistory> creditHistories = getCreditHistory(payment.getCustomerId());
        List<String> failureMessages = new ArrayList<>();
        PaymentEvent paymentEvent =
                paymentDomainService.validateAndInitiatePayment(payment, creditEntry, creditHistories, failureMessages);
        persistDbObjects(payment, creditEntry, creditHistories, failureMessages);

        orderOutboxHelper.saveOrderOutboxMessage(
                paymentDataMapper.paymentEventToOrderEventPayload(paymentEvent),
                paymentEvent.getPayment().getPaymentStatus(),
                OutboxStatus.Started,
                paymentRequest.getSagaId()
        );
    }

    @Transactional
    public void persistCancelPayment(PaymentRequest paymentRequest) {
        if (publishIfOutboxMessageProcessedForPayment(paymentRequest, PaymentStatus.Cancelled)) {
            if (log.isInfoEnabled()) {
                log.info("An outbox message with saga id: [{}] is already saved to database!", paymentRequest.getSagaId());
            }
            return;
        }

        if (log.isInfoEnabled()) {
            log.info("Received payment rollback event for order id: {}", paymentRequest.getOrderId());
        }
        Optional<Payment> paymentResponse = paymentRepository
                .findByOrderId(paymentRequest.getOrderId());
        if (paymentResponse.isEmpty()) {
            if (log.isErrorEnabled()) {
                log.error("Payment with order id: [{}] could not be found!", paymentRequest.getOrderId());
            }
            throw new PaymentNotFoundException("Payment with order id: " +
                    paymentRequest.getOrderId() + " could not be found!");
        }
        Payment payment = paymentResponse.get();
        CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        List<CreditHistory> creditHistories = getCreditHistory(payment.getCustomerId());
        List<String> failureMessages = new ArrayList<>();
        PaymentEvent paymentEvent = paymentDomainService
                .validateAndCancelPayment(payment, creditEntry, creditHistories, failureMessages);
        persistDbObjects(payment, creditEntry, creditHistories, failureMessages);

        orderOutboxHelper.saveOrderOutboxMessage(paymentDataMapper.paymentEventToOrderEventPayload(paymentEvent),
                paymentEvent.getPayment().getPaymentStatus(),
                OutboxStatus.Started,
                paymentRequest.getSagaId());

    }

    private CreditEntry getCreditEntry(CustomerId customerId) {
        Optional<CreditEntry> creditEntry = creditEntryRepository.findByCustomerId(customerId);
        if (creditEntry.isEmpty()) {
            if (log.isErrorEnabled()) {
                log.error("Could not find credit entry for customer: [{}]", customerId.getValue());
            }
            throw new PaymentApplicationServiceException("Could not find credit entry for customer: " +
                    customerId.getValue());
        }
        return creditEntry.get();
    }

    private List<CreditHistory> getCreditHistory(CustomerId customerId) {
        Optional<List<CreditHistory>> creditHistories = creditHistoryRepository.findByCustomerId(customerId);
        if (creditHistories.isEmpty()) {
            if (log.isErrorEnabled()) {
                log.error("Could not find credit history for customer: [{}]", customerId.getValue());
            }
            throw new PaymentApplicationServiceException("Could not find credit history for customer: " +
                    customerId.getValue());
        }
        return creditHistories.get();
    }

    private void persistDbObjects(Payment payment,
                                  CreditEntry creditEntry,
                                  List<CreditHistory> creditHistories,
                                  List<String> failureMessages) {
        paymentRepository.save(payment);
        if (failureMessages.isEmpty()) {
            creditEntryRepository.save(creditEntry);
            creditHistoryRepository.save(creditHistories.getLast());
        }
    }

    private boolean publishIfOutboxMessageProcessedForPayment(PaymentRequest paymentRequest,
                                                              PaymentStatus paymentStatus) {
        Optional<OrderOutboxMessage> orderOutboxMessage =
                orderOutboxHelper.getCompletedOrderOutboxMessageBySagaIdAndPaymentStatus(paymentRequest.getSagaId(), paymentStatus);

        if (orderOutboxMessage.isPresent()) {
            paymentResponseMessagePublisher.publish(orderOutboxMessage.get(), orderOutboxHelper::updateOutboxMessage);
            return true;
        }
        return false;
    }
}
