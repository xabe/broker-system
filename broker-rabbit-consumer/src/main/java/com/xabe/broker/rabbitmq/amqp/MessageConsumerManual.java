package com.xabe.broker.rabbitmq.amqp;

import com.rabbitmq.client.Channel;
import com.xabe.broker.rabbitmq.payload.MessagePayload;
import com.xabe.broker.rabbitmq.service.ServiceConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.MessageConverter;

public class MessageConsumerManual implements ChannelAwareMessageListener {

    private final Logger logger = LoggerFactory.getLogger(MessageConsumerManual.class);
    private final ServiceConsumer serviceConsumer;
    private final MessageConverter converter;

    public MessageConsumerManual(MessageConverter converter,ServiceConsumer serviceConsumer) {
        this.converter = converter;
        this.serviceConsumer = serviceConsumer;
    }

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        final long tag = message.getMessageProperties().getDeliveryTag();
        final MessagePayload result = (MessagePayload) converter.fromMessage(message);
        this.logger.info("Consumer messagePayload manual ack {}",message);
        this.serviceConsumer.add(result);
        channel.basicAck(tag, false);
        //channel.basicReject(tag,true);
    }
}
