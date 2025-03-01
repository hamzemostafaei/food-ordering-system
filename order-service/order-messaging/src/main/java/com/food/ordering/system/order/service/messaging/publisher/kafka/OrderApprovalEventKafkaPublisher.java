package com.food.ordering.system.order.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.ordering.system.kafka.producer.KafkaMessageHelper;
import com.food.ordering.system.kafka.producer.service.IKafkaProducer;
import com.food.ordering.system.order.service.domain.config.OrderServiceConfigData;
import com.food.ordering.system.order.service.domain.oubox.model.approval.OrderApprovalEventPayload;
import com.food.ordering.system.order.service.domain.oubox.model.approval.OrderApprovalOutboxMessage;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurant.approval.IRestaurantApprovalRequestMessagePublisher;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import com.food.ordering.system.outbox.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderApprovalEventKafkaPublisher implements IRestaurantApprovalRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final IKafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;

    @Override
    public void publish(OrderApprovalOutboxMessage orderApprovalOutboxMessage,
                        BiConsumer<OrderApprovalOutboxMessage, OutboxStatus> outboxCallback) {

        OrderApprovalEventPayload orderApprovalEventPayload = kafkaMessageHelper.getOrderEventPayload(
                orderApprovalOutboxMessage.getPayload(),
                OrderApprovalEventPayload.class
        );

        String sagaId = orderApprovalOutboxMessage.getSagaId();

        if (log.isInfoEnabled()) {
            log.info("Received OrderApprovalOutboxMessage for order id: [{}] and saga id: [{}]",
                    orderApprovalEventPayload.getOrderId(),
                    sagaId);
        }

        try {
            RestaurantApprovalRequestAvroModel restaurantApprovalRequestAvroModel =
                    orderMessagingDataMapper.orderApprovalEventToRestaurantApprovalRequestAvroModel(sagaId, orderApprovalEventPayload);

            kafkaProducer.send(
                    orderServiceConfigData.getRestaurantApprovalRequestTopicName(),
                    sagaId,
                    restaurantApprovalRequestAvroModel,
                    kafkaMessageHelper.getKafkaCallback(
                            orderServiceConfigData.getRestaurantApprovalRequestTopicName(),
                            restaurantApprovalRequestAvroModel,
                            orderApprovalOutboxMessage,
                            outboxCallback,
                            orderApprovalEventPayload.getOrderId(),
                            "RestaurantApprovalRequestAvroModel"
                    )
            );

            if (log.isInfoEnabled()) {
                log.info("OrderApprovalEventPayload sent to kafka for order id: [{}] and saga id: [{}]", restaurantApprovalRequestAvroModel.getOrderId(), sagaId);
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(
                        "Error while sending OrderApprovalEventPayload to kafka for order id: {} and saga id: {}, error: {}",
                        orderApprovalEventPayload.getOrderId(),
                        sagaId,
                        e.getMessage()
                );
            }
        }
    }
}
