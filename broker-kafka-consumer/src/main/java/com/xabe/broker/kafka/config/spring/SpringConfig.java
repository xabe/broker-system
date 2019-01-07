package com.xabe.broker.kafka.config.spring;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.xabe.broker.kafka.amqp.MessageConsumerAuto;
import com.xabe.broker.kafka.amqp.MessageConsumerManual;
import com.xabe.broker.kafka.payload.MessagePayload;
import com.xabe.broker.kafka.service.ServiceConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;


@Configuration
@ComponentScan(basePackages = {"com.xabe.broker.kafka.resource","com.xabe.broker.kafka.config.spring","com.xabe.broker.kafka.service","com.xabe.broker.kafka.amqp"})
@PropertySource("classpath:application.properties")
public class SpringConfig {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public Map<String, Object> consumerConfigs() {
        final Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "json");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        return props;
    }

    @Bean
    public ConsumerFactory<String, MessagePayload> consumerFactory() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(), new JsonDeserializer<>(MessagePayload.class,mapper));
    }



    @Bean
    public ConcurrentMessageListenerContainer<String, MessagePayload> kafkaListenerContainerFactory(@Value("${kafka.topic}") String topic,
                                                                                                         ObjectProvider<MessageConsumerAuto> messageListenerAuto,
                                                                                                         ObjectProvider<MessageConsumerManual> messageListenerManual) {
        MessageListener<String,MessagePayload> messageListener;
        ContainerProperties containerProperties;
        if(messageListenerAuto.getIfAvailable() != null){
            messageListener = messageListenerAuto.getIfAvailable();
            containerProperties = containerProperties(topic, messageListener);
            containerProperties.setAckMode(ContainerProperties.AckMode.RECORD);
        }
        else{
            messageListener = messageListenerManual.getIfAvailable();
            containerProperties = containerProperties(topic, messageListener);
            containerProperties.setAckMode(ContainerProperties.AckMode.MANUAL);
        }

        final ConcurrentMessageListenerContainer container = new ConcurrentMessageListenerContainer(consumerFactory(),containerProperties);
        container.start();
        return container;
    }

    private ContainerProperties containerProperties(String topic, MessageListener<String, MessagePayload> messageListener) {
        ContainerProperties containerProperties = new ContainerProperties(topic);
        containerProperties.setMessageListener(messageListener);
        return containerProperties;
    }

    @Bean
    @Profile("auto")
    public MessageConsumerAuto listenerAdapter(ServiceConsumer serviceConsumer) {
        return new MessageConsumerAuto(serviceConsumer);
    }

    @Bean
    @Profile("manual")
    public MessageConsumerManual listenerAdapterManual(ServiceConsumer serviceConsumer){
        return new MessageConsumerManual(serviceConsumer);
    }

    //@EnableKafka Auto scan @KafkaListener(topics = "test", groupId = "json", containerFactory = "kafkaListenerContainerFactory") processMessage(@Payload MessagePayload messagePayload)

//@Bean
//   public ConcurrentKafkaListenerContainerFactory<String, MessagePayload> kafkaListenerContainerFactory(){
//        final ConcurrentKafkaListenerContainerFactory<String, MessagePayload> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        factory.getContainerProperties().setMessageListener(messageListenerAuto.getIfAvailable());
//        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
//        factory.getContainerProperties().setAckOnError(false);
//        return factory;
//        }

}
