package com.xabe.broker.kafka.amqp;

import com.xabe.broker.kafka.payload.MessagePayload;

public interface ProducerExecutor {
    void execute(MessagePayload messagePayload);
}
