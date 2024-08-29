package com.food.ordering.system.payment.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.kafka.producer.service.IKafkaProducer;
import com.food.ordering.system.payment.service.domain.config.PaymentServiceConfigData;
import com.food.ordering.system.payment.service.domain.event.PaymentCompletedEvent;
import com.food.ordering.system.payment.service.domain.ports.output.message.publisher.IPaymentCompletedMessagePublisher;
import com.food.ordering.system.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentCompletedKafkaMessagePublisher implements IPaymentCompletedMessagePublisher {

    private final PaymentMessagingDataMapper paymentMessagingDataMapper;
    private final IKafkaProducer<String, PaymentResponseAvroModel> kafkaProducer;
    private final PaymentServiceConfigData paymentServiceConfigData;

    @Override
    public void publish(PaymentCompletedEvent paymentCompletedEvent) {
        String orderId = paymentCompletedEvent.getPayment().getId().getValue();
        log.info("Received PaymentCompletedEvent for orderId: {}", orderId);

        try {
            CompletableFuture<SendResult<String, PaymentResponseAvroModel>> callbackFuture = new CompletableFuture<>();
            PaymentResponseAvroModel paymentResponseAvroModel = paymentMessagingDataMapper.paymentCompletedEventToPaymentResponseAvroModel(paymentCompletedEvent);
            String topicName = paymentServiceConfigData.getPaymentResponseTopicName();
            kafkaProducer.send(
                    topicName,
                    orderId,
                    paymentResponseAvroModel,
                    callbackFuture
            );

            log.info("paymentResponseAvroModel sent to Kafka for orderId: {}", orderId);
        } catch (Exception e) {
            log.error("Error while sending paymentResponseAvroModel to Kafka for orderId: {}", orderId, e);
        }
    }
}
