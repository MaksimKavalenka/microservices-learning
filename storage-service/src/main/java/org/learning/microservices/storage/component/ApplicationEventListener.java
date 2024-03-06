package org.learning.microservices.storage.component;

import lombok.RequiredArgsConstructor;
import org.learning.microservices.storage.api.domain.StorageRequest;
import org.learning.microservices.storage.controller.StorageController;
import org.learning.microservices.storage.repository.StorageRepository;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationEventListener {

    private final StorageRepository repository;

    private final StorageController controller;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!repository.existsByStorageType("staging")) {
            controller.createStorage(StorageRequest.builder()
                    .storageType("staging")
                    .bucketName("staging")
                    .path("/files")
                    .build());
        }

        if (!repository.existsByStorageType("permanent")) {
            controller.createStorage(StorageRequest.builder()
                    .storageType("permanent")
                    .bucketName("permanent")
                    .path("/files")
                    .build());
        }
    }

}
