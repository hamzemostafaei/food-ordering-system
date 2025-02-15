package com.food.ordering.system.restaurant.service.messaging.linstener.kafka;

import com.food.ordering.system.kafka.consumer.IKafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.ordering.system.restaurant.service.domain.exception.RestaurantNotFoundException;
import com.food.ordering.system.restaurant.service.domain.ports.input.message.listener.IRestaurantApprovalRequestMessageListener;
import com.food.ordering.system.restaurant.service.messaging.mapper.RestaurantMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantApprovalRequestKafkaListener implements IKafkaConsumer<RestaurantApprovalRequestAvroModel> {

    private final IRestaurantApprovalRequestMessageListener restaurantApprovalRequestMessageListener;
    private final RestaurantMessagingDataMapper restaurantMessagingDataMapper;

    @Override
    @KafkaListener(
            id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}",
            topics = "${restaurant-service.restaurant-approval-request-topic-name}"
    )
    public void receive(List<ConsumerRecord<String, RestaurantApprovalRequestAvroModel>> messages) {

        if (log.isInfoEnabled()) {
            log.info("{} number of orders approval requests received", messages.size());
        }

        messages
                .forEach(message -> {
                    String key = message.key();
                    int partition = message.partition();
                    long offset = message.offset();

                    if (log.isInfoEnabled()) {
                        log.info("Processing message with key: {}, partition: {}, offset: {}", key, partition, offset);
                    }

                    RestaurantApprovalRequestAvroModel restaurantApprovalRequestAvroModel = message.value();

                    try {
                        if (log.isInfoEnabled()) {
                            log.info("Processing order approval for order id: {}", restaurantApprovalRequestAvroModel.getOrderId());
                        }
                        restaurantApprovalRequestMessageListener.approveOrder(
                                restaurantMessagingDataMapper.restaurantApprovalRequestAvroModelToRestaurantApproval(restaurantApprovalRequestAvroModel)
                        );
                    } catch (RestaurantNotFoundException e) {
                        if (log.isErrorEnabled()) {
                            log.error("No restaurant found for restaurant id: {}, and order id: {}",
                                    restaurantApprovalRequestAvroModel.getRestaurantId(),
                                    restaurantApprovalRequestAvroModel.getOrderId());
                        }
                    }
                });

    }
}
