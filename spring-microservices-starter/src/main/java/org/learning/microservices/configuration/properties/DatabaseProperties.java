package org.learning.microservices.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("database.destination")
public class DatabaseProperties {

    private String host;

    private Integer port;

    private String name;

    private String schema;

}
