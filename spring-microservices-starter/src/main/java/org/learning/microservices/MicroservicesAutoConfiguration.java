package org.learning.microservices;

import org.learning.microservices.configuration.properties.DatabaseProperties;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@AutoConfigureBefore(FlywayAutoConfiguration.class)
@EnableConfigurationProperties(DatabaseProperties.class)
public class MicroservicesAutoConfiguration {
}
