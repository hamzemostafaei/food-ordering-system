package com.food.ordering.system.payment.service.domain.outbox.scheduler;

import com.food.ordering.system.outbox.IOutboxScheduler;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.payment.service.domain.outbox.model.OrderOutboxMessage;
import com.food.ordering.system.payment.service.domain.ports.output.message.publisher.IPaymentResponseMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderOutboxScheduler implements IOutboxScheduler {

    private final OrderOutboxHelper orderOutboxHelper;
    private final IPaymentResponseMessagePublisher paymentResponseMessagePublisher;

    @Override
    @Transactional
    @Scheduled(fixedRateString = "${payment-service.outbox-scheduler-fixed-rate}",
            initialDelayString = "${payment-service.outbox-scheduler-initial-delay}")
    public void processOutboxMessage() {

        Optional<List<OrderOutboxMessage>> outboxMessagesResponse =
                orderOutboxHelper.getOrderOutboxMessageByOutboxStatus(OutboxStatus.Started);

        if (outboxMessagesResponse.isPresent() && !outboxMessagesResponse.get().isEmpty()) {
            List<OrderOutboxMessage> outboxMessages = outboxMessagesResponse.get();

            log.info("Received [{}] OrderOutboxMessage with ids [{}], sending to message bus!", outboxMessages.size(),
                    outboxMessages.stream().map(OrderOutboxMessage::getId).collect(Collectors.joining(",")));

            outboxMessages.forEach(orderOutboxMessage ->
                    paymentResponseMessagePublisher.publish(orderOutboxMessage,
                            orderOutboxHelper::updateOutboxMessage));
            log.info("{} OrderOutboxMessage sent to message bus!", outboxMessages.size());
        }
    }
}
