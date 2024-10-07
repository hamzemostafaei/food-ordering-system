package com.food.ordering.system.customer.service.messaging.publisher.kafka;

import com.food.ordering.system.customer.service.domain.config.CustomerServiceConfigData;
import com.food.ordering.system.customer.service.domain.event.CustomerCreatedEvent;
import com.food.ordering.system.customer.service.domain.ports.output.message.publisher.ICustomerMessagePublisher;
import com.food.ordering.system.customer.service.messaging.mapper.CustomerMessagingDataMapper;
import com.food.ordering.system.kafka.order.avro.model.CustomerAvroModel;
import com.food.ordering.system.kafka.producer.KafkaMessageHelper;
import com.food.ordering.system.kafka.producer.service.IKafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerCreatedEventKafkaPublisher implements ICustomerMessagePublisher {

    private final CustomerMessagingDataMapper customerMessagingDataMapper;
    private final IKafkaProducer<String, CustomerAvroModel> kafkaProducer;
    private final CustomerServiceConfigData customerServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;

    @Override
    public void publish(CustomerCreatedEvent customerCreatedEvent) {
        log.info("Received CustomerCreatedEvent for customer id: [{}]", customerCreatedEvent.getCustomer().getId().getValue());
        try {
            CustomerAvroModel customerAvroModel = customerMessagingDataMapper.paymentResponseAvroModelToPaymentResponse(customerCreatedEvent);

            kafkaProducer.send(
                    customerServiceConfigData.getCustomerTopicName(),
                    customerAvroModel.getId(),
                    customerAvroModel,
                    getCallback(customerServiceConfigData.getCustomerTopicName(), customerAvroModel)
            );

            log.info("CustomerCreatedEvent sent to kafka for customer id: {}",
                    customerAvroModel.getId());
        } catch (Exception e) {
            log.error("Error while sending CustomerCreatedEvent to kafka for customer id: {}," +
                    " error: {}", customerCreatedEvent.getCustomer().getId().getValue(), e.getMessage());
        }
    }

    private CompletableFuture<SendResult<String, CustomerAvroModel>> getCallback(String topicName, CustomerAvroModel message) {

        CompletableFuture<SendResult<String, CustomerAvroModel>> completableFuture = new CompletableFuture<>();

        completableFuture.whenComplete((result, throwable) -> {
            if (throwable != null) {
                log.error("Error while sending message {} to topic {}", message.toString(), topicName, throwable);
            } else {
                RecordMetadata metadata = result.getRecordMetadata();
                log.info("Received new metadata. Topic: {}; Partition {}; Offset {}; Timestamp {}, at time {}",
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset(),
                        metadata.timestamp(),
                        System.nanoTime());
            }
        });

        return completableFuture;
    }
}
