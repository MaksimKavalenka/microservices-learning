package org.learning.microservices.processor.configuration;

import org.learning.microservices.storage.api.domain.StorageResponse;
import org.learning.microservices.storage.api.service.StorageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class StorageConfiguration {

    @Bean
    public Map<String, StorageResponse> storages(StorageService storageService) {
        return storageService.getStorages().stream()
                .collect(Collectors.toMap(StorageResponse::getStorageType, Function.identity()));
    }

}
