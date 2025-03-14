package com.food.ordering.system.order.service.domain.oubox.scheduler.approval;

import com.food.ordering.system.order.service.domain.oubox.model.approval.OrderApprovalOutboxMessage;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurant.approval.IRestaurantApprovalRequestMessagePublisher;
import com.food.ordering.system.outbox.IOutboxScheduler;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.saga.SagaStatus;
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
public class RestaurantApprovalOutboxScheduler implements IOutboxScheduler {

    private final ApprovalOutboxHelper approvalOutboxHelper;
    private final IRestaurantApprovalRequestMessagePublisher restaurantApprovalRequestMessagePublisher;

    @Override
    @Transactional
    @Scheduled(fixedDelayString = "${order-service.outbox-scheduler-fixed-rate}", initialDelayString = "${order-service.outbox-scheduler-initial-delay}")
    public void processOutboxMessage() {
        Optional<List<OrderApprovalOutboxMessage>> outboxMessagesResponse =
                approvalOutboxHelper.getApprovalOutboxMessageByOutboxStatusAndSagaStatus(
                        OutboxStatus.Started,
                        SagaStatus.Processing);
        if (outboxMessagesResponse.isPresent() && !outboxMessagesResponse.get().isEmpty()) {
            List<OrderApprovalOutboxMessage> outboxMessages = outboxMessagesResponse.get();
            if (log.isInfoEnabled()) {
                log.info("Received [{}] OrderApprovalOutboxMessage with ids: [{}], sending to message bus!",
                        outboxMessages.size(),
                        outboxMessages.stream()
                                .map(OrderApprovalOutboxMessage::getId)
                                .collect(Collectors.joining(","))
                );
            }
            outboxMessages.forEach(outboxMessage ->
                    restaurantApprovalRequestMessagePublisher.publish(outboxMessage, this::updateOutboxStatus));
            if (log.isInfoEnabled()) {
                log.info("[{}] OrderApprovalOutboxMessage sent to message bus!", outboxMessages.size());
            }

        }
    }

    private void updateOutboxStatus(OrderApprovalOutboxMessage orderApprovalOutboxMessage, OutboxStatus outboxStatus) {
        orderApprovalOutboxMessage.setOutboxStatus(outboxStatus);
        approvalOutboxHelper.save(orderApprovalOutboxMessage);
        if (log.isInfoEnabled()) {
            log.info("OrderApprovalOutboxMessage is updated with outbox status: [{}]", outboxStatus.name());
        }
    }
}