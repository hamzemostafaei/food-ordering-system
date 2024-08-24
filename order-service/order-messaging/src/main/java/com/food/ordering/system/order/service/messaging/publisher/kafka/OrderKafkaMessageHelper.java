package com.food.ordering.system.order.service.messaging.publisher.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderKafkaMessageHelper {

    public <T extends SpecificRecord> Callback getKafkaCallback(String topicName, T request, String id) {
        return (metadata, exception) -> {
            if (exception != null) {
                log.error("Error while sending request {} to topic {}", request, topicName, exception);
            } else {
                log.info("Received successful response from kafka for order id: {} Topic: {} Partition: {} Offset: {} Timestamp: {}",
                        id,
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset(),
                        metadata.timestamp()
                );
            }
        };
    }

    public <K extends String, V extends SpecificRecord> ProducerRecord<K, V> getProducerRecord(String topicName, K key, V paymentRequestAvroModel) {
        return new ProducerRecord<>(topicName, key, paymentRequestAvroModel);
    }
}
