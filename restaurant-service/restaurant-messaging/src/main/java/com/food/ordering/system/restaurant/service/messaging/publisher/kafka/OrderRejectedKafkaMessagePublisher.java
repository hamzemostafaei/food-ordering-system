package com.food.ordering.system.restaurant.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.kafka.producer.service.IKafkaProducer;
import com.food.ordering.system.restaurant.service.domain.config.RestaurantServiceConfigData;
import com.food.ordering.system.restaurant.service.domain.event.OrderRejectedEvent;
import com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.IOrderRejectedMessagePublisher;
import com.food.ordering.system.restaurant.service.messaging.mapper.RestaurantMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderRejectedKafkaMessagePublisher implements IOrderRejectedMessagePublisher {

    private final RestaurantMessagingDataMapper restaurantMessagingDataMapper;
    private final IKafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer;
    private final RestaurantServiceConfigData restaurantServiceConfigData;

    @Override
    public void publish(OrderRejectedEvent orderRejectedEvent) {
        String orderId = orderRejectedEvent.getOrderApproval().getOrderId().getValue();

        log.info("Received OrderRejectedEvent for orderId: {}", orderId);

        try {
            CompletableFuture<SendResult<String, RestaurantApprovalResponseAvroModel>> callbackFuture = new CompletableFuture<>();
            RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel =
                    restaurantMessagingDataMapper.orderRejectedEventToRestaurantApprovalAvroModel(orderRejectedEvent);
            String topicName = restaurantServiceConfigData.getRestaurantApprovalResponseTopicName();
            kafkaProducer.send(
                    topicName,
                    orderId,
                    restaurantApprovalResponseAvroModel,
                    callbackFuture
            );

            log.info("OrderRejectedEvent sent to Kafka for orderId: {}", orderId);
        } catch (Exception e) {
            log.error("Error while sending OrderRejectedEvent to Kafka for orderId: {}", orderId, e);
        }
    }
}
