package org.learning.microservices.processor.client;

import org.learning.microservices.storage.api.service.StorageService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "storage-service", url = "${infrastructure.storage-service.url:}")
public interface StorageServiceFeignClient extends StorageService {
}
