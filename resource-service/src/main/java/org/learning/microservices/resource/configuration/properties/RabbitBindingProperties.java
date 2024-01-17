package org.learning.microservices.resource.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties("spring.rabbitmq")
public class RabbitBindingProperties {

    public static final String DELETE_BINDING_KEY = "delete";

    public static final String PROCESS_BINDING_KEY = "process";

    private Map<String, BindingProperties> bindings;

    @Data
    public static class BindingProperties {

        private String source;

        private String routingKey;


    }

}
