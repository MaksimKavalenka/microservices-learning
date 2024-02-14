package org.learning.microservices.storage.api.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.learning.microservices.storage.api.domain.StorageRequest;
import org.learning.microservices.storage.api.domain.StorageResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

public interface StorageService {

    @PostMapping("/api/v1/storages")
    Map<String, Object> createStorage(@RequestBody StorageRequest storageRequest);

    @GetMapping("/api/v1/storages/{id}")
    StorageResponse getStorage(@PathVariable("id") @Positive Integer id);

    @DeleteMapping("/api/v1/storages")
    Map<String, Object> deleteStorage(@RequestParam("id") @NotBlank String id);

}
