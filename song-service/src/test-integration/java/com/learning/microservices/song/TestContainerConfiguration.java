package com.learning.microservices.song;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.learning.microservices.configuration.properties.DatabaseProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;

@TestConfiguration
public class TestContainerConfiguration {

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgreSqlContainer(DatabaseProperties databaseProperties) {
        return new PostgreSQLContainer<>("postgres:15")
                .withDatabaseName(databaseProperties.getName())
                .withCreateContainerCmdModifier(
                        cmd -> cmd.getHostConfig().withPortBindings(
                                new PortBinding(Ports.Binding.bindPort(5432), new ExposedPort(5432))));
    }

    @Bean
    @ServiceConnection
    public RabbitMQContainer rabbitmqContainer() {
        return new RabbitMQContainer("rabbitmq:3.12");
    }

}
