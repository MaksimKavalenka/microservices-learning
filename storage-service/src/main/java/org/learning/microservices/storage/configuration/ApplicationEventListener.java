package org.learning.microservices.storage.configuration;

import lombok.RequiredArgsConstructor;
import org.learning.microservices.storage.api.domain.StorageRequest;
import org.learning.microservices.storage.controller.StorageController;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationEventListener {

    private final StorageController controller;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        controller.createStorage(StorageRequest.builder()
                .storageType("staging")
                .bucketName("staging")
                .path("/files")
                .build());

        controller.createStorage(StorageRequest.builder()
                .storageType("permanent")
                .bucketName("permanent")
                .path("/files")
                .build());
    }

}
