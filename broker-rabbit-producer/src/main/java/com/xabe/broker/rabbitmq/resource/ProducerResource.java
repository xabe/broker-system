package com.xabe.broker.rabbitmq.resource;

import com.xabe.broker.rabbitmq.payload.MessagePayload;
import com.xabe.broker.rabbitmq.service.ServiceProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;

@Component
@Singleton
@Path("/producer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProducerResource {

    private final ServiceProducer serviceProducer;

    @Autowired
    public ProducerResource(ServiceProducer serviceProducer) {
        this.serviceProducer = serviceProducer;
    }

    @POST
    @Path("/message")
    public JsonObject producerMessage(){
        this.serviceProducer.createMessage(new MessagePayload("MessagePayload",(int)(Math.random()*100), LocalDateTime.now()));
        final JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("status", "OK");
        return builder.build();
    }
}
