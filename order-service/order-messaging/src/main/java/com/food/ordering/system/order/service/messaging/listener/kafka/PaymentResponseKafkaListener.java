package com.food.ordering.system.order.service.messaging.listener.kafka;

import com.food.ordering.system.kafka.consumer.IKafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.kafka.order.avro.model.PaymentStatus;
import com.food.ordering.system.order.service.domain.exception.OrderNotFoundException;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.payment.IPaymentResponseMessageListener;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentResponseKafkaListener implements IKafkaConsumer<PaymentResponseAvroModel> {

    private final IPaymentResponseMessageListener paymentResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    @Override
    @KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}", topics = "${order-service.payment-response-topic-name}")
    public void receive(List<ConsumerRecord<String, PaymentResponseAvroModel>> messages) {

        log.info("{} number of payment requests received", messages.size());

        messages.forEach(message -> {
            String key = message.key();
            int partition = message.partition();
            long offset = message.offset();

            log.info("Processing message with key: [{}], partition: [{}], offset: [{}]", key, partition, offset);

            PaymentResponseAvroModel paymentResponseAvroModel = message.value();

            try {
                if (PaymentStatus.COMPLETED == paymentResponseAvroModel.getPaymentStatus()) {
                    log.info("Processing successful payment for order id: [{}]", paymentResponseAvroModel.getOrderId());
                    paymentResponseMessageListener.paymentCompleted(orderMessagingDataMapper.paymentResponseAvroModelToPaymentResponse(paymentResponseAvroModel));
                } else if (PaymentStatus.CANCELLED == paymentResponseAvroModel.getPaymentStatus() || PaymentStatus.FAILED == paymentResponseAvroModel.getPaymentStatus()) {
                    log.info("Processing unsuccessful payment for order id: [{}]", paymentResponseAvroModel.getOrderId());
                    paymentResponseMessageListener.paymentCancelled(orderMessagingDataMapper.paymentResponseAvroModelToPaymentResponse(paymentResponseAvroModel));
                }
            } catch (OptimisticLockingFailureException e) {
                //NO-OP for optimistic lock. This means another thread finished the work, do not throw error to prevent reading the data from kafka again!
                log.error("Caught optimistic locking exception in PaymentResponseKafkaListener for order id: [{}]", paymentResponseAvroModel.getOrderId());
            } catch (OrderNotFoundException e) {
                //NO-OP for OrderNotFoundException
                log.error("No order found for order id: [{}]", paymentResponseAvroModel.getOrderId());
            }
        });
    }

}
