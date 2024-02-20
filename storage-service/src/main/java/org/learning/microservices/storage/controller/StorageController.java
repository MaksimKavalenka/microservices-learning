package org.learning.microservices.storage.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.learning.microservices.storage.api.domain.StorageRequest;
import org.learning.microservices.storage.api.domain.StorageResponse;
import org.learning.microservices.storage.entity.StorageEntity;
import org.learning.microservices.storage.mapper.StorageMapper;
import org.learning.microservices.storage.repository.StorageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/v1/storages")
@RequiredArgsConstructor
@Slf4j
@Transactional
@Validated
public class StorageController {

    private final StorageMapper mapper;

    private final StorageRepository repository;

    private final S3ClientBuilder s3ClientBuilder;

    @Value("${application.deletion.limit:200}")
    private int deletionLimit;

    @Retryable
    @PostMapping
    public Map<String, Object> createStorage(@RequestBody StorageRequest storageRequest) {
        StorageEntity storage = mapper.toStorageEntity(storageRequest);

        try (S3Client s3Client = s3ClientBuilder.build()) {
            s3Client.createBucket(CreateBucketRequest.builder()
                    .bucket(storage.getBucketName())
                    .build());
            log.info("Storage is created: {}", storage.getBucketName());

            storage = repository.save(storage);
            log.info("Storage is saved: {}", storage.getId());

            return Map.of("id", storage.getId());
        }
    }

    @GetMapping
    public List<StorageResponse> getStorages() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(mapper::toStorageResponse)
                .toList();
    }

    @Retryable
    @DeleteMapping
    public Map<String, Object> deleteStorage(@RequestParam("id") @NotBlank String id) {
        List<Integer> ids = Arrays.stream(id.split(","))
                .map(Integer::valueOf)
                .toList();

        if (ids.size() > deletionLimit) {
            throw new IllegalArgumentException(
                    String.format("It is not allowed to delete more than %d storages in a single request", deletionLimit));
        }

        ids = repository.getExistingIds(ids);
        repository.deleteAllById(ids);
        log.info("Songs are deleted: {}", ids);

        return Map.of("ids", ids);
    }

}
