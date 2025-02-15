package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.value.object.OrderId;
import com.food.ordering.system.domain.value.object.OrderStatus;
import com.food.ordering.system.domain.value.object.PaymentStatus;
import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.exception.OrderNotFoundException;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.oubox.model.approval.OrderApprovalOutboxMessage;
import com.food.ordering.system.order.service.domain.oubox.model.payment.OrderPaymentOutboxMessage;
import com.food.ordering.system.order.service.domain.oubox.scheduler.approval.ApprovalOutboxHelper;
import com.food.ordering.system.order.service.domain.oubox.scheduler.payment.PaymentOutboxHelper;
import com.food.ordering.system.order.service.domain.ports.output.repository.IOrderRepository;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.saga.ISagaStep;
import com.food.ordering.system.saga.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static com.food.ordering.system.domain.DomainConstants.UTC;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPaymentSaga implements ISagaStep<PaymentResponse> {

    private final IOrderDomainService orderDomainService;
    private final IOrderRepository orderRepository;
    private final OrderSagaHelper orderSagaHelper;
    private final PaymentOutboxHelper paymentOutboxHelper;
    private final ApprovalOutboxHelper approvalOutboxHelper;
    private final OrderDataMapper orderDataMapper;

    @Override
    @Transactional
    public void process(PaymentResponse paymentResponse) {
        Optional<OrderPaymentOutboxMessage> orderPaymentOutboxMessageResponse = paymentOutboxHelper.getPaymentOutboxMessageBySagaIdAndSagaStatus(
                paymentResponse.getSagaId(),
                SagaStatus.Started
        );

        if (orderPaymentOutboxMessageResponse.isEmpty()) {
            if (log.isInfoEnabled()) {
                log.info("An outbox message with saga id: [{}] is already processed!", paymentResponse.getSagaId());
            }
            return;
        }

        OrderPaymentOutboxMessage orderPaymentOutboxMessage = orderPaymentOutboxMessageResponse.get();

        OrderPaidEvent domainEvent = completePaymentForOrder(paymentResponse);

        SagaStatus sagaStatus = orderSagaHelper.orderStatusToSagaStatus(domainEvent.getOrder().getOrderStatus());

        paymentOutboxHelper.save(getUpdatedPaymentOutboxMessage(
                orderPaymentOutboxMessage,
                domainEvent.getOrder().getOrderStatus(), sagaStatus)
        );

        approvalOutboxHelper.saveApprovalOutboxMessage(
                orderDataMapper.orderPaidEventToOrderApprovalEventPayload(domainEvent),
                domainEvent.getOrder().getOrderStatus(),
                sagaStatus,
                OutboxStatus.Started,
                paymentResponse.getSagaId()
        );

        if (log.isInfoEnabled()) {
            log.info("Order with id: [{}] is paid", domainEvent.getOrder().getId().getValue());
        }
    }

    @Override
    @Transactional
    public void rollback(PaymentResponse paymentResponse) {

        Optional<OrderPaymentOutboxMessage> orderPaymentOutboxMessageResponse =
                paymentOutboxHelper.getPaymentOutboxMessageBySagaIdAndSagaStatus(
                        paymentResponse.getSagaId(),
                        getCurrentSagaStatus(paymentResponse.getPaymentStatus()));

        if (orderPaymentOutboxMessageResponse.isEmpty()) {
            if (log.isInfoEnabled()) {
                log.info("An outbox message with saga id: [{}] is already roll backed!", paymentResponse.getSagaId());
            }
            return;
        }

        OrderPaymentOutboxMessage orderPaymentOutboxMessage = orderPaymentOutboxMessageResponse.get();

        Order order = rollbackPaymentForOrder(paymentResponse);

        SagaStatus sagaStatus = orderSagaHelper.orderStatusToSagaStatus(order.getOrderStatus());

        paymentOutboxHelper.save(getUpdatedPaymentOutboxMessage(orderPaymentOutboxMessage,
                order.getOrderStatus(), sagaStatus));

        if (paymentResponse.getPaymentStatus() == PaymentStatus.Cancelled) {
            approvalOutboxHelper.save(
                    getUpdatedApprovalOutboxMessage(
                            paymentResponse.getSagaId(),
                            order.getOrderStatus(),
                            sagaStatus
                    )
            );
        }

        if (log.isInfoEnabled()) {
            log.info("Order with id: [{}] is cancelled", order.getId().getValue());
        }
    }

    private Order findOrder(String orderId) {
        Optional<Order> orderResponse = orderRepository.findById(new OrderId(orderId));
        if (orderResponse.isEmpty()) {
            if (log.isErrorEnabled()) {
                log.error("Order with id: [{}] could not be found!", orderId);
            }
            throw new OrderNotFoundException("Order with id " + orderId + " could not be found!");
        }
        return orderResponse.get();
    }

    private OrderPaymentOutboxMessage getUpdatedPaymentOutboxMessage(OrderPaymentOutboxMessage orderPaymentOutboxMessage,
                                                                     OrderStatus orderStatus,
                                                                     SagaStatus sagaStatus) {
        orderPaymentOutboxMessage.setProcessedAt(ZonedDateTime.now(ZoneId.of(UTC)));
        orderPaymentOutboxMessage.setOrderStatus(orderStatus);
        orderPaymentOutboxMessage.setSagaStatus(sagaStatus);
        return orderPaymentOutboxMessage;
    }

    private OrderPaidEvent completePaymentForOrder(PaymentResponse paymentResponse) {
        if (log.isInfoEnabled()) {
            log.info("Completing payment for order with id: [{}]", paymentResponse.getOrderId());
        }
        Order order = findOrder(paymentResponse.getOrderId());
        OrderPaidEvent domainEvent = orderDomainService.payOrder(order);
        orderRepository.save(order);
        return domainEvent;
    }

    private SagaStatus[] getCurrentSagaStatus(PaymentStatus paymentStatus) {
        return switch (paymentStatus) {
            case Completed -> new SagaStatus[]{SagaStatus.Started};
            case Cancelled -> new SagaStatus[]{SagaStatus.Processing};
            case Failed -> new SagaStatus[]{SagaStatus.Started, SagaStatus.Processing};
        };
    }

    private Order rollbackPaymentForOrder(PaymentResponse paymentResponse) {
        if (log.isInfoEnabled()) {
            log.info("Cancelling order with id: [{}]", paymentResponse.getOrderId());
        }
        Order order = findOrder(paymentResponse.getOrderId());
        orderDomainService.cancelOrder(order, paymentResponse.getFailureMessages());
        orderRepository.save(order);
        return order;
    }

    private OrderApprovalOutboxMessage getUpdatedApprovalOutboxMessage(String sagaId,
                                                                       OrderStatus orderStatus,
                                                                       SagaStatus sagaStatus) {
        Optional<OrderApprovalOutboxMessage> orderApprovalOutboxMessageResponse =
                approvalOutboxHelper.getApprovalOutboxMessageBySagaIdAndSagaStatus(
                        sagaId,
                        SagaStatus.Compensating);
        if (orderApprovalOutboxMessageResponse.isEmpty()) {
            throw new OrderDomainException("Approval outbox message could not be found in " + SagaStatus.Compensating.getName() + " status!");
        }
        OrderApprovalOutboxMessage orderApprovalOutboxMessage = orderApprovalOutboxMessageResponse.get();
        orderApprovalOutboxMessage.setProcessedAt(ZonedDateTime.now(ZoneId.of(UTC)));
        orderApprovalOutboxMessage.setOrderStatus(orderStatus);
        orderApprovalOutboxMessage.setSagaStatus(sagaStatus);
        return orderApprovalOutboxMessage;
    }
}
