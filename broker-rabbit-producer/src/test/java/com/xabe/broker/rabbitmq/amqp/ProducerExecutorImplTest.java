package com.xabe.broker.rabbitmq.amqp;

import com.xabe.broker.rabbitmq.payload.MessagePayload;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpTemplate;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ProducerExecutorImplTest {

    @Test
    public void shouldSendMessageBroker() throws Exception {
        //Given
        final MessagePayload messagePayload = new MessagePayload("a", 1, LocalDateTime.now());
        final AmqpTemplate amqpTemplate = mock(AmqpTemplate.class);
        final ProducerExecutor producerExecutor = new ProducerExecutorImpl(amqpTemplate, "");

        //When
        producerExecutor.execute(messagePayload);

        //Then
        verify(amqpTemplate).convertAndSend(anyString(), any(MessagePayload.class));

    }

}