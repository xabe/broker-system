package com.xabe.broker.rabbitmq.service;

import com.xabe.broker.rabbitmq.amqp.ProducerExecutor;
import com.xabe.broker.rabbitmq.payload.MessagePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ServiceProducerImpl implements ServiceProducer{

    private final static Logger LOGGER = LoggerFactory.getLogger(ServiceProducerImpl.class);
    private final ProducerExecutor producerExecutor;

    @Autowired
    public ServiceProducerImpl(ProducerExecutor producerExecutor) {
        this.producerExecutor = producerExecutor;
    }

    @Override
    @Async
    public void createMessage(MessagePayload messagePayload) {
        LOGGER.info("Send messagePayload {}", messagePayload);
        this.producerExecutor.execute(messagePayload);
    }
}
