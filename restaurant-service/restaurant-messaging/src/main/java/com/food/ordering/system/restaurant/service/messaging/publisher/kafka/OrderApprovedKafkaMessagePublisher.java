package com.food.ordering.system.restaurant.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.kafka.producer.service.IKafkaProducer;
import com.food.ordering.system.restaurant.service.domain.config.RestaurantServiceConfigData;
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovedEvent;
import com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.IOrderApprovedMessagePublisher;
import com.food.ordering.system.restaurant.service.messaging.mapper.RestaurantMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderApprovedKafkaMessagePublisher implements IOrderApprovedMessagePublisher {

    private final RestaurantMessagingDataMapper restaurantMessagingDataMapper;
    private final IKafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer;
    private final RestaurantServiceConfigData restaurantServiceConfigData;

    @Override
    public void publish(OrderApprovedEvent orderApprovedEvent) {
        String orderId = orderApprovedEvent.getOrderApproval().getOrderId().getValue();

        log.info("Received OrderApprovedEvent for orderId: {}", orderId);

        try {
            CompletableFuture<SendResult<String, RestaurantApprovalResponseAvroModel>> callbackFuture = new CompletableFuture<>();
            RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel =
                    restaurantMessagingDataMapper.orderApprovedEventToRestaurantApprovalAvroModel(orderApprovedEvent);
            String topicName = restaurantServiceConfigData.getRestaurantApprovalResponseTopicName();
            kafkaProducer.send(
                    topicName,
                    orderId,
                    restaurantApprovalResponseAvroModel,
                    callbackFuture
            );

            log.info("OrderApprovedEvent sent to Kafka for orderId: {}", orderId);
        } catch (Exception e) {
            log.error("Error while sending OrderApprovedEvent to Kafka for orderId: {}", orderId, e);
        }
    }
}
