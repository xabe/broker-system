package com.xabe.broker.kafka.service;

import com.xabe.broker.kafka.payload.MessagePayload;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ServiceConsumerImpl implements ServiceConsumer {

    private final List<MessagePayload> messages;

    public ServiceConsumerImpl() {
        messages = Collections.synchronizedList(new ArrayList());
    }

    @Override
    public List<MessagePayload> getMessages() {
        return messages;
    }

    @Override
    @Async
    public void add(MessagePayload message) {
        this.messages.add(message);
    }

}
