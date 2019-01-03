package com.xabe.broker.rabbitmq.service;

import com.xabe.broker.rabbitmq.amqp.ProducerExecutor;
import com.xabe.broker.rabbitmq.payload.MessagePayload;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ServiceProducerImplTest {


    @Test
    public void givenAMessageWhenInvokeProducerMessage() throws Exception {
        //Given
        final MessagePayload messagePayload = new MessagePayload("m",1, LocalDateTime.now());
        final ProducerExecutor producerExecutor = mock(ProducerExecutor.class);
        final ServiceProducer serviceProducer = new ServiceProducerImpl(producerExecutor);

        //When
        serviceProducer.createMessage(messagePayload);

        //Then
        verify(producerExecutor).execute(any());
    }

}