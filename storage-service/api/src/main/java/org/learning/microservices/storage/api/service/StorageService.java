package org.learning.microservices.storage.api.service;

import jakarta.validation.constraints.NotBlank;
import org.learning.microservices.storage.api.domain.StorageRequest;
import org.learning.microservices.storage.api.domain.StorageResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

public interface StorageService {

    @PostMapping("/api/v1/storages")
    Map<String, Object> createStorage(@RequestBody StorageRequest storageRequest);

    @GetMapping("/api/v1/storages")
    List<StorageResponse> getStorages();

    @DeleteMapping("/api/v1/storages")
    Map<String, Object> deleteStorage(@RequestParam("id") @NotBlank String id);

}
