package org.learning.microservices.resource.client;

import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.learning.microservices.storage.api.domain.StorageRequest;
import org.learning.microservices.storage.api.domain.StorageResponse;
import org.springframework.cloud.client.circuitbreaker.NoFallbackAvailableException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class StorageServiceFeignClientFallback implements StorageServiceFeignClient {

    @Override
    public Map<String, Object> createStorage(StorageRequest storageRequest) {
        throw new NoFallbackAvailableException("Not supported", new UnsupportedOperationException());
    }

    @Override
    public List<StorageResponse> getStorages() {
        log.info("Storage Service is unavailable. The fallback mechanism is triggered.");

        return List.of(
                StorageResponse.builder()
                        .storageType("staging")
                        .bucketName("staging")
                        .path("/files")
                        .build(),

                StorageResponse.builder()
                        .storageType("permanent")
                        .bucketName("permanent")
                        .path("/files")
                        .build());
    }

    @Override
    public Map<String, Object> deleteStorage(@NotBlank String s) {
        throw new NoFallbackAvailableException("Not supported", new UnsupportedOperationException());
    }

}
