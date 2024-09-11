package com.food.ordering.system.order.service.messaging.listener.kafka;

import com.food.ordering.system.kafka.consumer.IKafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.OrderApprovalStatus;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.order.service.domain.exception.OrderNotFoundException;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.restaurant.approval.IRestaurantApprovalResponseMessageListener;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.food.ordering.system.order.service.domain.entity.Order.FAILURE_MESSAGE_DELIMITER;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantApprovalResponseKafkaListener implements IKafkaConsumer<RestaurantApprovalResponseAvroModel> {

    private final IRestaurantApprovalResponseMessageListener restaurantApprovalResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    @Override
    @KafkaListener(id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}", topics = "${order-service.restaurant-approval-response-topic-name}")
    public void receive(List<ConsumerRecord<String, RestaurantApprovalResponseAvroModel>> messages) {

        log.info("{} number of restaurant approval responses received", messages.size());

        messages
                .forEach(message -> {
                    String key = message.key();
                    int partition = message.partition();
                    long offset = message.offset();

                    log.info("Processing message with key: {}, partition: {}, offset: {}", key, partition, offset);

                    RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel = message.value();
                    try {
                        if (OrderApprovalStatus.APPROVED == restaurantApprovalResponseAvroModel.getOrderApprovalStatus()) {
                            log.info("Processing approved order for order id: {}", restaurantApprovalResponseAvroModel.getOrderId());
                            restaurantApprovalResponseMessageListener.orderApproved(orderMessagingDataMapper.approvalResponseAvroModelToApprovalResponse(restaurantApprovalResponseAvroModel));
                        } else if (OrderApprovalStatus.REJECTED == restaurantApprovalResponseAvroModel.getOrderApprovalStatus()) {
                            log.info("Processing rejected order for order id: {}, with failure messages: {}", restaurantApprovalResponseAvroModel.getOrderId(), String.join(FAILURE_MESSAGE_DELIMITER, restaurantApprovalResponseAvroModel.getFailureMessages()));
                            restaurantApprovalResponseMessageListener.orderRejected(orderMessagingDataMapper.approvalResponseAvroModelToApprovalResponse(restaurantApprovalResponseAvroModel));
                        }
                    } catch (OptimisticLockingFailureException e) {
                        //NO-OP for optimistic lock. This means another thread finished the work, do not throw error to prevent reading the data from kafka again!
                        log.error("Caught optimistic locking exception in RestaurantApprovalResponseKafkaListener for order id: {}",
                                restaurantApprovalResponseAvroModel.getOrderId());
                    } catch (OrderNotFoundException e) {
                        //NO-OP for OrderNotFoundException
                        log.error("No order found for order id: {}", restaurantApprovalResponseAvroModel.getOrderId());
                    }
                });

    }
}
