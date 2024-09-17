package com.food.ordering.system.payment.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.kafka.producer.service.IKafkaProducer;
import com.food.ordering.system.payment.service.domain.config.PaymentServiceConfigData;
import com.food.ordering.system.payment.service.domain.event.PaymentFailedEvent;
import com.food.ordering.system.payment.service.domain.ports.output.message.publisher.IPaymentFailedMessagePublisher;
import com.food.ordering.system.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentFailedKafkaMessagePublisher implements IPaymentFailedMessagePublisher {

    private final PaymentMessagingDataMapper paymentMessagingDataMapper;
    private final IKafkaProducer<String, PaymentResponseAvroModel> kafkaProducer;
    private final PaymentServiceConfigData paymentServiceConfigData;

    @Override
    public void publish(PaymentFailedEvent paymentFailedEvent) {
        String orderId = paymentFailedEvent.getPayment().getId().getValue();
        log.info("Received PaymentFailedEvent for orderId: [{}]", orderId);

        try {
            CompletableFuture<SendResult<String, PaymentResponseAvroModel>> callbackFuture = new CompletableFuture<>();
            PaymentResponseAvroModel paymentResponseAvroModel = paymentMessagingDataMapper.paymentFailedEventToPaymentResponseAvroModel(paymentFailedEvent);
            String topicName = paymentServiceConfigData.getPaymentResponseTopicName();
            kafkaProducer.send(
                    topicName,
                    orderId,
                    paymentResponseAvroModel,
                    callbackFuture
            );

            log.info("PaymentFailedEvent sent to Kafka for orderId: [{}]", orderId);
        } catch (Exception e) {
            log.error("Error while sending PaymentFailedEvent to Kafka for orderId: [{}]", orderId, e);
        }
    }
}
