package com.food.ordering.system.payment.service.messaging.listener.kafka;

import com.food.ordering.system.kafka.consumer.IKafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.PaymentOrderStatus;
import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordering.system.payment.service.domain.ports.input.message.listener.IPaymentRequestMessageListener;
import com.food.ordering.system.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentRequestKafkaListener implements IKafkaConsumer<PaymentRequestAvroModel> {

    private final IPaymentRequestMessageListener paymentRequestMessageListener;
    private final PaymentMessagingDataMapper paymentMessagingDataMapper;

    @Override
    @KafkaListener(
            id = "${kafka-consumer-config.payment-consumer-group-id}",
            topics = "${payment-service.payment-request-topic-name}"
    )
    public void receive(List<ConsumerRecord<String, PaymentRequestAvroModel>> messages) {

        log.info("{} number of payment requests received", messages.size());

        messages
                .forEach(message -> {
                    String key = message.key();
                    int partition = message.partition();
                    long offset = message.offset();

                    log.info("Processing message with key: {}, partition: {}, offset: {}", key, partition, offset);

                    PaymentRequestAvroModel paymentRequestAvroModel = message.value();

                    if (PaymentOrderStatus.PENDING == paymentRequestAvroModel.getPaymentOrderStatus()) {
                        log.info("Processing payment for order id: {}", paymentRequestAvroModel.getOrderId());
                        paymentRequestMessageListener.completePayment(
                                paymentMessagingDataMapper.paymentRequestAvroModelToPaymentRequest(paymentRequestAvroModel)
                        );
                    } else if (PaymentOrderStatus.CANCELLED == paymentRequestAvroModel.getPaymentOrderStatus()) {
                        log.info("Cancelling payment for order id: {}", paymentRequestAvroModel.getOrderId());
                        paymentRequestMessageListener.cancelPayment(
                                paymentMessagingDataMapper.paymentRequestAvroModelToPaymentRequest(paymentRequestAvroModel));
                    }
                });
    }
}
