package com.xabe.broker.rabbitmq.resource;

import com.xabe.broker.rabbitmq.payload.MessagePayload;
import com.xabe.broker.rabbitmq.service.ServiceConsumer;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConsumerResourceTest {

    @Test
    public void shouldGetAllMessages() throws Exception {
        //Given
        final ServiceConsumer serviceConsumer = mock(ServiceConsumer.class);
        final ConsumerResource consumerResource = new ConsumerResource(serviceConsumer);
        when(serviceConsumer.getMessages()).thenReturn(Collections.emptyList());

        //When
        List<MessagePayload> result = consumerResource.getMessage();

        //Then
        MatcherAssert.assertThat(result, Matchers.is(Matchers.notNullValue()));

    }

}