package com.xabe.broker.kafka.amqp;

import com.xabe.broker.kafka.payload.MessagePayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProducerExecutorImpl implements  ProducerExecutor {

    private final KafkaTemplate<String,MessagePayload> kafkaTemplate;
    private final String topic;

    @Autowired
    public ProducerExecutorImpl(KafkaTemplate<String,MessagePayload> kafkaTemplate, @Value("${kafka.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void execute(MessagePayload messagePayload) {
        this.kafkaTemplate.send(topic, messagePayload);
    }
}
