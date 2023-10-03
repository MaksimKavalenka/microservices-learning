package org.learning.resource.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("database")
public class DatabaseProperties {

    private String host;

    private Integer port;

    private String name;

}
