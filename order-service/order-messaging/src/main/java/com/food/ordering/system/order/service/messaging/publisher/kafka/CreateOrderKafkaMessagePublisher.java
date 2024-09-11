package com.food.ordering.system.order.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordering.system.kafka.producer.service.IKafkaProducer;
import com.food.ordering.system.order.service.domain.config.OrderServiceConfigData;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment.IOrderCreatedPaymentRequestMessagePublisher;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateOrderKafkaMessagePublisher implements IOrderCreatedPaymentRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData orderServiceConfigData;
    private final IKafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;

    @Override
    public void publish(OrderCreatedEvent domainEvent) {
        String orderId = domainEvent.getOrder().getId().getValue();
        log.info("Received OrderCreatedEvent for orderId: {}", orderId);

        try {
            CompletableFuture<SendResult<String, PaymentRequestAvroModel>> callbackFuture = new CompletableFuture<>();
            PaymentRequestAvroModel paymentRequestAvroModel = orderMessagingDataMapper.orderCreatedEventToPaymentRequestAvroModel(domainEvent);
            String topicName = orderServiceConfigData.getPaymentRequestTopicName();
            kafkaProducer.send(
                    topicName,
                    orderId,
                    paymentRequestAvroModel,
                    callbackFuture
            );

            log.info("PaymentRequestAvroModel sent to Kafka for orderId: {}", orderId);
        } catch (Exception e) {
            log.error("Error while sending PaymentRequestAvroModel to Kafka for orderId: {}", orderId, e);
        }
    }

}
