package org.learning.resource.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.learning.resource.domain.Resource;
import org.learning.resource.exception.ResourceNotFoundException;
import org.learning.resource.repository.ResourceRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceRepository repository;

    @PostMapping(consumes = "audio/mpeg")
    public Map<String, Object> uploadResource(@RequestBody byte[] content) {
        Resource resource = Resource.builder().content(content).build();
        resource = repository.save(resource);
        return Map.of("id", resource.getId());
    }

    @GetMapping("/{id}")
    public byte[] getResource(@PathVariable("id") @NonNull Integer id) {
        return repository.findById(id)
                .map(Resource::getContent)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @DeleteMapping
    public Map<String, Object> deleteResources(@RequestParam("id") @NonNull String id) {
        List<Integer> ids = Arrays.stream(id.split(","))
                .map(Integer::valueOf)
                .toList();

        repository.deleteAllById(ids);
        return Map.of("ids", ids);
    }

}
