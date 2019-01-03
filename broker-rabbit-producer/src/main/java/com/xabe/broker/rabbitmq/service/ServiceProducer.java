package com.xabe.broker.rabbitmq.service;

import com.xabe.broker.rabbitmq.payload.MessagePayload;

public interface ServiceProducer {
    void createMessage(MessagePayload messagePayload);
}
