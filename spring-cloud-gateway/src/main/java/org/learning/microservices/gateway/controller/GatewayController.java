package org.learning.microservices.gateway.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.learning.microservices.gateway.client.ResourceServiceFeignClient;
import org.learning.microservices.gateway.client.SongServiceFeignClient;
import org.learning.microservices.song.api.domain.SongResponse;
import org.springframework.retry.annotation.Retryable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/gateway")
@RequiredArgsConstructor
@Slf4j
@Validated
public class GatewayController {

    private final ResourceServiceFeignClient resourceService;

    private final SongServiceFeignClient songService;

    @Retryable
    @PostMapping(consumes = "audio/mpeg")
    public Map<String, Object> uploadResource(@RequestBody byte[] content) {
        return resourceService.uploadResource(content);
    }

    @GetMapping("/{id}")
    public byte[] getResource(@PathVariable("id") @Positive Integer id) {
        return resourceService.getResource(id);
    }

    @GetMapping("/{id}/metadata")
    public SongResponse getResourceMetadata(@PathVariable("id") @Positive Integer id) {
        return songService.getSong(id);
    }

    @DeleteMapping
    public Map<String, Object> deleteResources(@RequestParam("id") @NotBlank String id) {
        return resourceService.deleteResources(id);
    }

}
