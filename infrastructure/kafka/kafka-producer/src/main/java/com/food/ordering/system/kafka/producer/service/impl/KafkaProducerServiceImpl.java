package com.food.ordering.system.kafka.producer.service.impl;

import com.food.ordering.system.kafka.producer.exception.KafkaProducerException;
import com.food.ordering.system.kafka.producer.service.IKafkaProducerService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerServiceImpl<K extends Serializable, V extends SpecificRecordBase> implements IKafkaProducerService<K, V> {

    private final KafkaTemplate<K, V> kafkaTemplate;

    @Override
    public void send(String topicName, K key, V message, CompletableFuture<SendResult<K, V>> callback) {
        log.info("Sending message={} to topic={}", message, topicName);
        try {
            CompletableFuture<SendResult<K, V>> kafkaCompletableFuture = kafkaTemplate.send(topicName, key, message);

            kafkaCompletableFuture.whenComplete((result, throwable) -> {
                if (throwable != null) {
                    log.error("Error sending message to Kafka with key: {}, message: {}. Exception: {}", key, message, throwable.getMessage());
                    callback.completeExceptionally(throwable);
                } else {
                    log.info("Message sent successfully to topic={}, partition={}, offset={}",
                            result.getRecordMetadata().topic(),
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                    callback.complete(result);
                }
            });
        } catch (KafkaException e) {
            log.error("Error on kafka producer with key: {}, message: {} and exception: {}", key, message, e.getMessage());
            throw new KafkaProducerException(String.format("Error on kafka producer with key: %s  and message: %s", key, message));
        }
    }

    @PreDestroy
    public void close() {
        if (kafkaTemplate != null) {
            log.info("Closing kafka producer!");
            kafkaTemplate.destroy();
        }
    }
}
