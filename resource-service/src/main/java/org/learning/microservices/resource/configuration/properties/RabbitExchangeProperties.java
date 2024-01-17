package org.learning.microservices.resource.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("spring.rabbitmq.exchange")
public class RabbitExchangeProperties {

    private String name;

    private String routingKey;


}
