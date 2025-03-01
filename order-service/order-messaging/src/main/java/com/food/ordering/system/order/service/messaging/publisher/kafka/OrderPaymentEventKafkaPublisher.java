package com.food.ordering.system.order.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordering.system.kafka.producer.KafkaMessageHelper;
import com.food.ordering.system.kafka.producer.service.IKafkaProducer;
import com.food.ordering.system.order.service.domain.config.OrderServiceConfigData;
import com.food.ordering.system.order.service.domain.oubox.model.payment.OrderPaymentEventPayload;
import com.food.ordering.system.order.service.domain.oubox.model.payment.OrderPaymentOutboxMessage;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment.IPaymentRequestMessagePublisher;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import com.food.ordering.system.outbox.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPaymentEventKafkaPublisher implements IPaymentRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final IKafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;

    @Override
    public void publish(OrderPaymentOutboxMessage orderPaymentOutboxMessage, BiConsumer<OrderPaymentOutboxMessage, OutboxStatus> outboxCallback) {

        OrderPaymentEventPayload orderPaymentEventPayload = kafkaMessageHelper.getOrderEventPayload(
                orderPaymentOutboxMessage.getPayload(),
                OrderPaymentEventPayload.class
        );

        String sagaId = orderPaymentOutboxMessage.getSagaId();

        if (log.isInfoEnabled()) {
            log.info("Received OrderPaymentOutboxMessage for order id: [{}] and saga id: [{}]",
                    orderPaymentEventPayload.getOrderId(),
                    sagaId);
        }

        try {

            PaymentRequestAvroModel paymentRequestAvroModel =
                    orderMessagingDataMapper.orderPaymentEventToPaymentRequestAvroModel(sagaId, orderPaymentEventPayload);

            kafkaProducer.send(
                    orderServiceConfigData.getPaymentRequestTopicName(),
                    sagaId,
                    paymentRequestAvroModel,
                    kafkaMessageHelper.getKafkaCallback(
                            orderServiceConfigData.getPaymentRequestTopicName(),
                            paymentRequestAvroModel,
                            orderPaymentOutboxMessage,
                            outboxCallback,
                            orderPaymentEventPayload.getOrderId(),
                            "PaymentRequestAvroModel"
                    )
            );

            if (log.isInfoEnabled()) {
                log.info("OrderPaymentEventPayload sent to Kafka for order id: [{}] and saga id: [{}]", orderPaymentEventPayload.getOrderId(), sagaId);
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Error while sending OrderPaymentEventPayload to kafka with order id: [{}] and saga id: [{}], error: [{}]",
                        orderPaymentEventPayload.getOrderId(),
                        sagaId,
                        e.getMessage()
                );
            }
        }
    }
}
