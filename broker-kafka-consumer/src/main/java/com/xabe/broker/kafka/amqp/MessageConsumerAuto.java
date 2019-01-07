package com.xabe.broker.kafka.amqp;

import com.xabe.broker.kafka.payload.MessagePayload;
import com.xabe.broker.kafka.service.ServiceConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.MessageListener;

public class MessageConsumerAuto implements MessageListener<String,MessagePayload> {

    private final Logger logger = LoggerFactory.getLogger(MessageConsumerAuto.class);
    private final ServiceConsumer serviceConsumer;

    public MessageConsumerAuto(ServiceConsumer serviceConsumer) {
        this.serviceConsumer = serviceConsumer;
    }

    @Override
    public void onMessage(ConsumerRecord<String, MessagePayload> consumerRecord) {
        this.logger.info("Consumer messagePayload auto ack {}",consumerRecord);
        this.serviceConsumer.add(consumerRecord.value());
    }
}
