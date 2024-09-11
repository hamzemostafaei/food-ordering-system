package com.food.ordering.system.kafka.consumer;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;

public interface IKafkaConsumer<T extends SpecificRecordBase> {
    void receive(List<ConsumerRecord<String, T>> messages);
}
