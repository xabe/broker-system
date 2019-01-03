package com.xabe.broker.rabbitmq.resource;

import com.xabe.broker.rabbitmq.payload.MessagePayload;
import com.xabe.broker.rabbitmq.service.ServiceConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/consumer")
@Singleton
@Component
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class ConsumerResource {

    private final ServiceConsumer serviceConsumer;

    @Autowired
    public ConsumerResource(ServiceConsumer serviceConsumer) {
        this.serviceConsumer = serviceConsumer;
    }

    @Path("/message")
    @GET
    public List<MessagePayload> getMessage(){
        return this.serviceConsumer.getMessages();
    }
}
