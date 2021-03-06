package com.xabe.broker.kafka.config;

import com.xabe.broker.kafka.config.jackson.ObjectMapperContextResolver;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.spring.SpringLifecycleListener;
import org.springframework.context.ApplicationContext;
import org.springframework.web.filter.RequestContextFilter;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/")
public class CustomResourceConfig extends ResourceConfig {

    private static final String CONTEXT_CONFIG = "contextConfig";

    public CustomResourceConfig(ApplicationContext applicationContext) {
        packages("com.xabe.broker.kafka.resource");
        register(JacksonFeature.class);
        register(new ObjectMapperContextResolver());
        register(SpringLifecycleListener.class);
        register(RequestContextFilter.class);
        property( CONTEXT_CONFIG, applicationContext );
        property( ServerProperties.BV_FEATURE_DISABLE, true );
        property( ServerProperties.RESOURCE_VALIDATION_IGNORE_ERRORS, true );
        property( ServerProperties.TRACING, "ALL" );
        property( ServerProperties.TRACING_THRESHOLD, "VERBOSE" );
        property(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, true);
    }
}
