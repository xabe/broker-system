package com.xabe.broker.rabbitmq.service;

import com.xabe.broker.rabbitmq.payload.MessagePayload;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

class ServiceConsumerImplTest {

    private ServiceConsumer serviceConsumer;

    @BeforeEach
    public void setUp() throws Exception {
        this.serviceConsumer = new ServiceConsumerImpl();
    }

    @Test
    public void shouldGetListMessage() throws Exception {
        //Given

        //When
        final List<MessagePayload> result = this.serviceConsumer.getMessages();

        //Then
        MatcherAssert.assertThat(result, Matchers.is(Matchers.notNullValue()));

    }

    @Test
    public void shouldAddListMessage() throws Exception {
        //Given
        final MessagePayload message = new MessagePayload("s",1, LocalDateTime.now());

        //When
        this.serviceConsumer.add(message);
        final List<MessagePayload> result = this.serviceConsumer.getMessages();

        //Then
        MatcherAssert.assertThat(result, Matchers.is(Matchers.notNullValue()));
        MatcherAssert.assertThat(result, Matchers.is(Matchers.hasSize(1)));

    }

}