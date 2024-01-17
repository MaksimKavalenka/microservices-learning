package org.learning.microservices.resource.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.learning.microservices.exception.DataNotFoundException;
import org.learning.microservices.resource.api.ResourceMessage;
import org.learning.microservices.resource.configuration.properties.RabbitExchangeProperties;
import org.learning.microservices.resource.domain.ResourceEntity;
import org.learning.microservices.resource.repository.ResourceRepository;
import org.learning.microservices.resource.service.AwsS3Service;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/resources")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ResourceController {

    private final AwsS3Service awsS3Service;

    private final RabbitExchangeProperties rabbitExchangeProperties;

    private final RabbitTemplate rabbitTemplate;

    private final ResourceRepository repository;

    @Value("${application.deletion.limit:200}")
    private int deletionLimit;

    @PostMapping(consumes = "audio/mpeg")
    public Map<String, Object> uploadResource(@RequestBody byte[] content) throws IOException {
        String s3Key = RandomStringUtils.randomAlphanumeric(16);

        // Write a resource to S3
        Path path = Paths.get(s3Key);
        Files.write(path, content);
        awsS3Service.putObject(s3Key, path);
        Files.delete(path);

        // Save a resource in DB
        ResourceEntity resource = ResourceEntity.builder().s3Key(s3Key).build();
        resource = repository.save(resource);
        log.info("Resource is saved: {}", resource.getId());

        ResourceMessage message = ResourceMessage.builder()
                .id(resource.getId())
                .s3Key(resource.getS3Key())
                .build();

        rabbitTemplate.convertAndSend(
                rabbitExchangeProperties.getName(), rabbitExchangeProperties.getRoutingKey(), message);
        log.info("The message is sent: {}", message);

        return Map.of("id", resource.getId());
    }

    @GetMapping("/{id}")
    public byte[] getResource(@PathVariable("id") @Positive Integer id) {
        return repository.findById(id)
                .map(ResourceEntity::getS3Key)
                .map(s3Key -> {
                    try {
                        return awsS3Service.getObjectBytes(s3Key);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElseThrow(() -> new DataNotFoundException(id));
    }

    @DeleteMapping
    public Map<String, Object> deleteResources(@RequestParam("id") @NotBlank String id) {
        List<Integer> ids = Arrays.stream(id.split(","))
                .map(Integer::valueOf)
                .toList();

        if (ids.size() > deletionLimit) {
            throw new IllegalArgumentException(
                    String.format("It is not allowed to delete more than %d resources in a single request",
                            deletionLimit));
        }

        // Delete resources from S3
        List<String> s3Keys = repository.getS3Keys(ids);
        awsS3Service.deleteObjects(s3Keys);

        // Delete resources from DB
        repository.deleteAllById(ids);
        log.info("Resources are deleted: {}", ids);

        return Map.of("ids", ids);
    }

}
