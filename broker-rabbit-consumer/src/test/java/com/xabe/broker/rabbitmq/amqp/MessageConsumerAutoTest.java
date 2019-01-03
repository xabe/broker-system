package com.xabe.broker.rabbitmq.amqp;

import com.xabe.broker.rabbitmq.payload.MessagePayload;
import com.xabe.broker.rabbitmq.service.ServiceConsumer;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

class MessageConsumerAutoTest {

    @Test
    public void shouldAddMessageWhenInvokeProcessMessage() throws Exception {
        //Given
        final MessagePayload messagePayload = new MessagePayload("m",1, LocalDateTime.now());
        final ServiceConsumer serviceConsumer = mock(ServiceConsumer.class);
        final MessageConsumerAuto messageConsumerAuto = new MessageConsumerAuto(serviceConsumer);

        //When
        messageConsumerAuto.processMessage(messagePayload);

        //Then
        verify(serviceConsumer).add(any());
    }

}