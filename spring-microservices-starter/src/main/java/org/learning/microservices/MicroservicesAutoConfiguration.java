package org.learning.microservices;

import org.learning.microservices.configuration.AwsS3Configuration;
import org.learning.microservices.configuration.properties.AwsProperties;
import org.learning.microservices.configuration.properties.DatabaseProperties;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@AutoConfigureBefore({AwsS3Configuration.class, FlywayAutoConfiguration.class})
@EnableConfigurationProperties({AwsProperties.class, DatabaseProperties.class})
public class MicroservicesAutoConfiguration {
}
