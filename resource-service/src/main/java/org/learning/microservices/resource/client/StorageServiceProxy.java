package org.learning.microservices.resource.client;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.learning.microservices.storage.api.domain.StorageRequest;
import org.learning.microservices.storage.api.domain.StorageResponse;
import org.learning.microservices.storage.api.service.StorageService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StorageServiceProxy implements StorageService {

    private final StorageServiceFeignClient storageService;

    private final StorageServiceFeignClientFallback storageServiceFallback;

    @Override
    public Map<String, Object> createStorage(StorageRequest storageRequest) {
        try {
            return storageService.createStorage(storageRequest);
        } catch (Exception e) {
            return storageServiceFallback.createStorage(storageRequest);
        }
    }

    @Override
    public List<StorageResponse> getStorages() {
        try {
            return storageService.getStorages();
        } catch (Exception e) {
            return storageServiceFallback.getStorages();
        }
    }

    @Override
    public Map<String, Object> deleteStorage(@NotBlank String id) {
        try {
            return storageService.deleteStorage(id);
        } catch (Exception e) {
            return storageServiceFallback.deleteStorage(id);
        }
    }
}
