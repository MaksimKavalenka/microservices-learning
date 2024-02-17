package org.learning.microservices.resource.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.learning.microservices.exception.DataNotFoundException;
import org.learning.microservices.resource.api.message.DeleteResourcesMessage;
import org.learning.microservices.resource.api.message.ProcessResourceMessage;
import org.learning.microservices.resource.configuration.properties.RabbitBindingProperties;
import org.learning.microservices.resource.configuration.properties.RabbitBindingProperties.BindingProperties;
import org.learning.microservices.resource.entity.ResourceEntity;
import org.learning.microservices.resource.repository.ResourceRepository;
import org.learning.microservices.service.AwsS3Service;
import org.learning.microservices.storage.api.domain.StorageResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.learning.microservices.resource.configuration.properties.RabbitBindingProperties.DELETE_BINDING_KEY;
import static org.learning.microservices.resource.configuration.properties.RabbitBindingProperties.PROCESS_BINDING_KEY;

@RestController
@RequestMapping("/v1/resources")
@RequiredArgsConstructor
@Slf4j
@Transactional
@Validated
public class ResourceController {

    private final AwsS3Service awsS3Service;

    private final RabbitBindingProperties rabbitBindingProperties;

    private final RabbitTemplate rabbitTemplate;

    private final ResourceRepository repository;

    private final Map<String, StorageResponse> storages;

    @Value("${application.deletion.limit:200}")
    private int deletionLimit;

    @Retryable
    @PostMapping(consumes = "audio/mpeg")
    public Map<String, Object> uploadResource(@RequestBody byte[] content) {
        String s3Key = RandomStringUtils.randomAlphanumeric(16);

        // Write a resource to S3
        StorageResponse stagingStorage = storages.get("staging");
        awsS3Service.putObject(s3Key, stagingStorage.getBucketName(), content);

        // Save a resource in DB
        ResourceEntity resource = ResourceEntity.builder().s3Key(s3Key).build();
        resource = repository.save(resource);
        log.info("Resource is saved: {}", resource.getId());

        ProcessResourceMessage message = ProcessResourceMessage.builder()
                .id(resource.getId())
                .s3Key(s3Key)
                .build();

        BindingProperties bindingProperties = rabbitBindingProperties.getBindings().get(PROCESS_BINDING_KEY);
        rabbitTemplate.convertAndSend(bindingProperties.getSource(), bindingProperties.getRoutingKey(), message);
        log.info("The message is sent: {}", message);

        return Map.of("id", resource.getId());
    }

    @GetMapping("/{id}")
    public byte[] getResource(@PathVariable("id") @Positive Integer id) {
        return repository.findById(id)
                .map(ResourceEntity::getS3Key)
                .map(s3Key -> {
                    try {
                        StorageResponse permanentStorage = storages.get("permanent");
                        return awsS3Service.getObjectBytes(s3Key, permanentStorage.getBucketName());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElseThrow(() -> new DataNotFoundException(id));
    }

    @Retryable
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
        StorageResponse permanentStorage = storages.get("permanent");
        awsS3Service.deleteObjects(s3Keys, permanentStorage.getBucketName());

        // Delete resources from DB
        repository.deleteAllById(ids);
        log.info("Resources are deleted: {}", ids);

        DeleteResourcesMessage message = DeleteResourcesMessage.builder()
                .ids(id)
                .build();

        BindingProperties bindingProperties = rabbitBindingProperties.getBindings().get(DELETE_BINDING_KEY);
        rabbitTemplate.convertAndSend(bindingProperties.getSource(), bindingProperties.getRoutingKey(), message);
        log.info("The message is sent: {}", message);

        return Map.of("ids", ids);
    }

}
