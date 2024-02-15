package org.learning.microservices.processor.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties("spring.rabbitmq")
public class RabbitBindingProperties {

    public static final String PROCESSED_ACK_BINDING_KEY = "processed-ack";

    private Map<String, BindingProperties> bindings;

    @Data
    public static class BindingProperties {

        private String source;

        private String routingKey;


    }

}
