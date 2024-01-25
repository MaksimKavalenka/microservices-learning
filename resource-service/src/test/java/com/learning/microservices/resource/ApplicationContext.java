package com.learning.microservices.resource;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableAutoConfiguration
@EnableConfigurationProperties(value = { TestContainerConfiguration.class })
public class ApplicationContext {
}
