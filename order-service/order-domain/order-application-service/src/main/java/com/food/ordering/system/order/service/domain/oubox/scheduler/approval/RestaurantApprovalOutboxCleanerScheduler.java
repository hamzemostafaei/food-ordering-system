package com.food.ordering.system.order.service.domain.oubox.scheduler.approval;

import com.food.ordering.system.order.service.domain.oubox.model.approval.OrderApprovalOutboxMessage;
import com.food.ordering.system.outbox.IOutboxScheduler;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.saga.SagaStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RestaurantApprovalOutboxCleanerScheduler implements IOutboxScheduler {

    private final ApprovalOutboxHelper approvalOutboxHelper;

    public RestaurantApprovalOutboxCleanerScheduler(ApprovalOutboxHelper approvalOutboxHelper) {
        this.approvalOutboxHelper = approvalOutboxHelper;
    }

    @Override
    @Scheduled(cron = "@midnight")
    public void processOutboxMessage() {
        Optional<List<OrderApprovalOutboxMessage>> outboxMessagesResponse =
                approvalOutboxHelper.getApprovalOutboxMessageByOutboxStatusAndSagaStatus(
                        OutboxStatus.Completed,
                        SagaStatus.Succeeded,
                        SagaStatus.Failed,
                        SagaStatus.Compensated);
        if (outboxMessagesResponse.isPresent()) {
            List<OrderApprovalOutboxMessage> outboxMessages = outboxMessagesResponse.get();
            if (log.isInfoEnabled()) {
                log.info("Received [{}] OrderApprovalOutboxMessage for clean-up. The payloads: [{}]",
                        outboxMessages.size(),
                        outboxMessages.stream().map(OrderApprovalOutboxMessage::getPayload)
                                .collect(Collectors.joining("\n")));
            }
            approvalOutboxHelper.deleteApprovalOutboxMessageByOutboxStatusAndSagaStatus(
                    OutboxStatus.Completed,
                    SagaStatus.Succeeded,
                    SagaStatus.Failed,
                    SagaStatus.Compensated);
            if (log.isInfoEnabled()) {
                log.info("[{}] OrderApprovalOutboxMessage deleted!", outboxMessages.size());
            }
        }

    }
}