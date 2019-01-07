package com.xabe.broker.kafka.service;

import com.xabe.broker.kafka.payload.MessagePayload;

public interface ServiceProducer {
    void createMessage(MessagePayload messagePayload);
}
