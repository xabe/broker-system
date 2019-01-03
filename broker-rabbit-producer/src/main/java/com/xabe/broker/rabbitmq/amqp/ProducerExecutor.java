package com.xabe.broker.rabbitmq.amqp;

import com.xabe.broker.rabbitmq.payload.MessagePayload;

public interface ProducerExecutor {
    void execute(MessagePayload messagePayload);
}
