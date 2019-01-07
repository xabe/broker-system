package com.xabe.broker.kafka.service;

import com.xabe.broker.kafka.payload.MessagePayload;

import java.util.List;

public interface ServiceConsumer {
    List<MessagePayload> getMessages();

    void add(MessagePayload message);
}
