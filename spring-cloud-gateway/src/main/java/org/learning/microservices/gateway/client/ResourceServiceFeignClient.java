package org.learning.microservices.gateway.client;

import org.learning.microservices.resource.api.service.ResourceService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "resource-service", url = "${infrastructure.resource-service.url:}")
public interface ResourceServiceFeignClient extends ResourceService {
}
