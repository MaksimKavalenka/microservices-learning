package org.learning.microservices.resource.client;

import org.learning.microservices.storage.api.service.StorageService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "storage-service", url = "${infrastructure.storage-service.url:}",
        fallback = StorageServiceFeignClientFallback.class)
public interface StorageServiceFeignClient extends StorageService {
}
