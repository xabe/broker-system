package com.xabe.broker.rabbitmq.config.spring;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.xabe.broker.rabbitmq.amqp.MessageConsumerAuto;
import com.xabe.broker.rabbitmq.amqp.MessageConsumerManual;
import com.xabe.broker.rabbitmq.service.ServiceConsumer;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;


@Configuration
@ComponentScan(basePackages = {"com.xabe.broker.rabbitmq.resource","com.xabe.broker.rabbitmq.config.spring","com.xabe.broker.rabbitmq.service"})
@PropertySource("classpath:application.properties")
public class SpringConfig {

    @Value("${queue.name}")
    private String queueName;
    @Value("${exchange.name}")
    private String exchangeName;
    @Value("${rabbitmq.username}")
    private String username;
    @Value("${rabbitmq.password}")
    private String password;
    @Value("${rabbitmq.hosts}")
    private String host;
    @Value("${rabbitmq.ports}")
    private Integer port;


    @Bean
    public Queue queue() {
        // This queue has the following properties:
        // name: my_durable
        // durable: true
        // exclusive: false
        // auto_delete: false
        return new Queue(queueName, true, false, false);
    }

    @Bean
    public Exchange exchange() {
        return new DirectExchange(exchangeName,true, false);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(queueName);
    }

    @Bean
    public ConnectionFactory connectionFactory(){
        final CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setPort(port);
        cachingConnectionFactory.setHost(host);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        return cachingConnectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                                    ObjectProvider<MessageListenerAdapter> messageListenerAuto,
                                                    ObjectProvider<ChannelAwareMessageListener> messageListenerManual) {
        final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageConverter(messageConverter());
        if(messageListenerAuto.getIfAvailable() != null){
            container.setMessageListener(messageListenerAuto.getIfAvailable());
            container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        }
        else{
            container.setMessageListener(messageListenerManual.getIfAvailable());
            container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        }
        container.setPrefetchCount(1);
        container.setConcurrentConsumers(1);
        return container;
    }

    @Bean
    @Profile("auto")
    public MessageListenerAdapter listenerAdapter(ServiceConsumer serviceConsumer) {
        final MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(new MessageConsumerAuto(serviceConsumer), "processMessage");
        messageListenerAdapter.setMessageConverter(messageConverter());
        return messageListenerAdapter;
    }

    @Bean
    @Profile("manual")
    public ChannelAwareMessageListener listenerAdapterManual(ServiceConsumer serviceConsumer){
        return new MessageConsumerManual(messageConverter(),serviceConsumer);
    }

    @Bean
    public MessageConverter messageConverter() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return new Jackson2JsonMessageConverter(mapper);
    }

}
