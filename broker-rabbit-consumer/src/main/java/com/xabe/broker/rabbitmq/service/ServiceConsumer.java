package com.xabe.broker.rabbitmq.service;

import com.xabe.broker.rabbitmq.payload.MessagePayload;

import java.util.List;

public interface ServiceConsumer {
    List<MessagePayload> getMessages();

    void add(MessagePayload message);
}
