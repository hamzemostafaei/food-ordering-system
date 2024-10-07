package com.food.ordering.system.order.service.messaging.listener.kafka;

import com.food.ordering.system.kafka.consumer.IKafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.CustomerAvroModel;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.customer.ICustomerMessageListener;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerKafkaListener implements IKafkaConsumer<CustomerAvroModel> {

    private final ICustomerMessageListener customerMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;


    @Override
    @KafkaListener(id = "${kafka-consumer-config.customer-group-id}", topics = "${order-service.customer-topic-name}")
    public void receive(List<ConsumerRecord<String, CustomerAvroModel>> messages) {

        log.info("{} number of customer create messages received", messages.size());

        messages.forEach(message -> {
            String key = message.key();
            int partition = message.partition();
            long offset = message.offset();

            CustomerAvroModel customerAvroModel = message.value();

            log.info("Processing message with key: [{}], partition: [{}], offset: [{}]", key, partition, offset);

            customerMessageListener.customerCreated(orderMessagingDataMapper.customerAvroModeltoCustomerModel(customerAvroModel));
        });
    }
}
