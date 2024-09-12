package com.food.ordering.system.kafka.producer.service.impl;

import com.food.ordering.system.kafka.producer.exception.KafkaProducerException;
import com.food.ordering.system.kafka.producer.service.IKafkaProducer;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerImpl<K extends Serializable, V extends SpecificRecordBase> implements IKafkaProducer<K, V> {

    private final KafkaTemplate<K, V> kafkaTemplate;

    @Override
    public void send(String topicName, K key, V message, CompletableFuture<SendResult<K, V>> callback) {
        log.info("Sending message=[{}] to topic=[{}]", message, topicName);
        try {
            ProducerRecord<K, V> producerRecord = new ProducerRecord<>(topicName, key, message);

            CompletableFuture<SendResult<K, V>> kafkaCompletableFuture = kafkaTemplate.send(producerRecord);

            kafkaCompletableFuture.whenComplete((result, throwable) -> {
                if (throwable != null) {
                    log.error("Error sending message to Kafka with key: [{}], message: [{}]. Exception: [{}]", key, message, throwable.getMessage());
                    callback.completeExceptionally(throwable);
                } else {
                    log.info("Message with key: [{}] sent successfully to topic: [{}], partition: [{}], offset: [{}]",
                            key,
                            result.getRecordMetadata().topic(),
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset()
                    );

                    boolean completed = callback.complete(result);
                    if (completed) {
                        log.info("Received successful response from kafka for message with key: [{}], topic: [{}], partition: [{}], offset: [{}].",
                                key,
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset()
                        );
                    } else {
                        log.info("Sending message with key [{}] was unsuccessful", key);
                    }
                }
            });
        } catch (KafkaException e) {
            log.error("Error on kafka producer with key: [{}], message: [{}] and exception: [{}]", key, message, e.getMessage());
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
