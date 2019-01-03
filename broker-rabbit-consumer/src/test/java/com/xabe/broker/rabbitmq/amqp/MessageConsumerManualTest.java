package com.xabe.broker.rabbitmq.amqp;

import com.rabbitmq.client.Channel;
import com.xabe.broker.rabbitmq.payload.MessagePayload;
import com.xabe.broker.rabbitmq.service.ServiceConsumer;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConverter;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MessageConsumerManualTest {

    @Test
    public void shouldAddMessageWhenInvokeOnMessage() throws Exception {
        //Given
        final MessagePayload messagePayload = new MessagePayload("m",1, LocalDateTime.now());
        final ServiceConsumer serviceConsumer = mock(ServiceConsumer.class);
        final MessageConverter messageConverter = mock(MessageConverter.class);
        when(messageConverter.fromMessage(any())).thenReturn(messagePayload);
        final MessageProperties messageProperties = new MessageProperties();
        messageProperties.setDeliveryTag(1l);
        final Message message = new Message(new byte[]{},messageProperties);
        final Channel channel = mock(Channel.class);
        final MessageConsumerManual messageConsumerManual = new MessageConsumerManual(messageConverter, serviceConsumer);


        //When
        messageConsumerManual.onMessage(message,channel);

        //Then
        verify(serviceConsumer).add(any());
        verify(channel).basicAck(eq(1l),eq(false));
    }

}