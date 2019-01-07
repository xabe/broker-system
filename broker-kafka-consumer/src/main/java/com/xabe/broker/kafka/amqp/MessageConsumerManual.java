package com.xabe.broker.kafka.amqp;

import com.xabe.broker.kafka.payload.MessagePayload;
import com.xabe.broker.kafka.service.ServiceConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;


public class MessageConsumerManual implements AcknowledgingMessageListener<String, MessagePayload> {

    private final Logger logger = LoggerFactory.getLogger(MessageConsumerManual.class);
    private final ServiceConsumer serviceConsumer;

    public MessageConsumerManual(ServiceConsumer serviceConsumer) {
        this.serviceConsumer = serviceConsumer;
    }

    @Override
    public void onMessage(ConsumerRecord<String, MessagePayload> consumerRecord, Acknowledgment acknowledgment) {
        this.logger.info("Consumer messagePayload manual ack {} {}",consumerRecord,acknowledgment);
        this.serviceConsumer.add(consumerRecord.value());
        acknowledgment.acknowledge(); //send ack
    }
}
