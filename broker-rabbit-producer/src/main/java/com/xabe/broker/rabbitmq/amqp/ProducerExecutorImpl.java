package com.xabe.broker.rabbitmq.amqp;

import com.xabe.broker.rabbitmq.payload.MessagePayload;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProducerExecutorImpl implements  ProducerExecutor {

    private final AmqpTemplate amqpTemplate;
    private final String routingKey;

    @Autowired
    public ProducerExecutorImpl(AmqpTemplate amqpTemplate, @Value("${queue.name}") String routingKey) {
        this.amqpTemplate = amqpTemplate;
        this.routingKey = routingKey;
    }

    @Override
    public void execute(MessagePayload messagePayload) {
        this.amqpTemplate.convertAndSend(routingKey, messagePayload);
    }
}
