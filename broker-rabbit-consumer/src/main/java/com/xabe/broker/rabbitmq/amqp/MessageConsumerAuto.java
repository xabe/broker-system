package com.xabe.broker.rabbitmq.amqp;

import com.xabe.broker.rabbitmq.payload.MessagePayload;
import com.xabe.broker.rabbitmq.service.ServiceConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageConsumerAuto {

    private final Logger logger = LoggerFactory.getLogger(MessageConsumerAuto.class);
    private final ServiceConsumer serviceConsumer;

    public MessageConsumerAuto(ServiceConsumer serviceConsumer) {
        this.serviceConsumer = serviceConsumer;
    }

    public void processMessage(MessagePayload messagePayload) {
       this.logger.info("Consumer messagePayload auto ack {}",messagePayload);
       this.serviceConsumer.add(messagePayload);
    }
}
