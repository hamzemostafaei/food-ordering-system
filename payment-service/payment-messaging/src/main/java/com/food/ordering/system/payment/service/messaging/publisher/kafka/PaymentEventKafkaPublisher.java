package com.food.ordering.system.payment.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.kafka.producer.KafkaMessageHelper;
import com.food.ordering.system.kafka.producer.service.IKafkaProducer;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.payment.service.domain.config.PaymentServiceConfigData;
import com.food.ordering.system.payment.service.domain.outbox.model.OrderEventPayload;
import com.food.ordering.system.payment.service.domain.outbox.model.OrderOutboxMessage;
import com.food.ordering.system.payment.service.domain.ports.output.message.publisher.IPaymentResponseMessagePublisher;
import com.food.ordering.system.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventKafkaPublisher implements IPaymentResponseMessagePublisher {

    private final PaymentMessagingDataMapper paymentMessagingDataMapper;
    private final IKafkaProducer<String, PaymentResponseAvroModel> kafkaProducer;
    private final PaymentServiceConfigData paymentServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;

    @Override
    public void publish(OrderOutboxMessage orderOutboxMessage, BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback) {

        OrderEventPayload orderEventPayload =
                kafkaMessageHelper.getOrderEventPayload(orderOutboxMessage.getPayload(), OrderEventPayload.class);

        String sagaId = orderOutboxMessage.getSagaId();

        if (log.isInfoEnabled()) {
            log.info("Received OrderOutboxMessage for order id: [{}] and saga id: [{}]",
                    orderEventPayload.getOrderId(),
                    sagaId);
        }

        try {

            PaymentResponseAvroModel paymentResponseAvroModel = paymentMessagingDataMapper.orderEventPayloadToPaymentResponseAvroModel(sagaId, orderEventPayload);

            kafkaProducer.send(
                    paymentServiceConfigData.getPaymentResponseTopicName(),
                    sagaId,
                    paymentResponseAvroModel,
                    kafkaMessageHelper.getKafkaCallback(
                            paymentServiceConfigData.getPaymentResponseTopicName(),
                            paymentResponseAvroModel,
                            orderOutboxMessage,
                            outboxCallback,
                            orderEventPayload.getOrderId(),
                            "PaymentResponseAvroModel"
                    )
            );

            if (log.isInfoEnabled()) {
                log.info("PaymentResponseAvroModel sent to kafka for order id: [{}] and saga id: [{}]",
                        paymentResponseAvroModel.getOrderId(), sagaId);
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Error while sending PaymentRequestAvroModel message to kafka with order id: [{}] and saga id: [{}], error:[{}]",
                        orderEventPayload.getOrderId(), sagaId, e.getMessage());
            }
        }
    }
}
