package com.xabe.broker.rabbitmq.resource;

import com.xabe.broker.rabbitmq.service.ServiceProducer;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import javax.json.JsonObject;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ProducerResourceTest {

    @Test
    public void shouldCreateMessageBroker() throws Exception {
        //Given
        final ServiceProducer serviceProducer = mock(ServiceProducer.class);
        final ProducerResource producerResource = new ProducerResource(serviceProducer);


        //When
        final Object result = producerResource.producerMessage();

        //Then
        MatcherAssert.assertThat(result, Matchers.is(Matchers.notNullValue()));
        verify(serviceProducer).createMessage(any());
    }

}