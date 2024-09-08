package com.food.ordering.system.restaurant.service.messaging.linstener.kafka;

import com.food.ordering.system.kafka.consumer.IKafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.ordering.system.restaurant.service.domain.exception.RestaurantNotFoundException;
import com.food.ordering.system.restaurant.service.domain.ports.input.message.listener.IRestaurantApprovalRequestMessageListener;
import com.food.ordering.system.restaurant.service.messaging.mapper.RestaurantMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
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
    public void receive(@Payload List<RestaurantApprovalRequestAvroModel> messages,
                        @Header List<String> keys,
                        @Header List<Integer> partitions,
                        @Header List<Long> offsets) {

        log.info("{} number of orders approval requests received with keys {}, partitions {} and offsets {}, sending for restaurant approval",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        messages
                .forEach(restaurantApprovalRequestAvroModel -> {
                    try {
                        log.info("Processing order approval for order id: {}", restaurantApprovalRequestAvroModel.getOrderId());
                        restaurantApprovalRequestMessageListener.approveOrder(
                                restaurantMessagingDataMapper.restaurantApprovalRequestAvroModelToRestaurantApproval(restaurantApprovalRequestAvroModel)
                        );
                    } catch (RestaurantNotFoundException e) {
                        log.error("No restaurant found for restaurant id: {}, and order id: {}",
                                restaurantApprovalRequestAvroModel.getRestaurantId(),
                                restaurantApprovalRequestAvroModel.getOrderId());
                    }
                });

    }
}
