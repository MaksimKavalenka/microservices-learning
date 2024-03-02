package com.learning.microservices.resource;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.learning.microservices.configuration.properties.DatabaseProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

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

    @Bean
    public LocalStackContainer localStackContainer() {
        return new LocalStackContainer(DockerImageName.parse("localstack/localstack:3.1"))
                .withServices(LocalStackContainer.Service.S3)
                .withCreateContainerCmdModifier(
                        cmd -> cmd.getHostConfig().withPortBindings(
                                new PortBinding(Ports.Binding.bindPort(4566), new ExposedPort(4566))));
    }

    @Bean
    public GenericContainer<?> authorizationServerContainer() {
        return new GenericContainer<>("maksimkavalenka/microservices-learning.authorization-server:latest")
                .withExposedPorts(8090)
                .withCreateContainerCmdModifier(
                        cmd -> cmd.getHostConfig().withPortBindings(
                                new PortBinding(Ports.Binding.bindPort(8090), new ExposedPort(8090))));
    }

}
