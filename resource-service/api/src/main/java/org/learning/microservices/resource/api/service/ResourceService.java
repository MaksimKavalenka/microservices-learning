package org.learning.microservices.resource.api.service;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

public interface ResourceService {

    @PostMapping(path = "/api/v1/resources", consumes = "audio/mpeg")
    Map<String, Object> uploadResource(@RequestBody byte[] content);

    @GetMapping("/api/v1/resources/{id}")
    byte[] getResource(@PathVariable("id") Integer id);

    @DeleteMapping("/api/v1/resources")
    Map<String, Object> deleteResources(@RequestParam("id") String id);


}
