package org.learning.microservices.resource.api.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ResourceService {

    @GetMapping("/api/v1/resources/{id}")
    byte[] getResource(@PathVariable("id") Integer id);

}
