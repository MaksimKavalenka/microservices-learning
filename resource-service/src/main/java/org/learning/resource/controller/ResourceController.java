package org.learning.resource.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.learning.microservices.exception.DataNotFoundException;
import org.learning.microservices.song.api.client.SongFeignClient;
import org.learning.microservices.song.api.domain.SongRequest;
import org.learning.resource.domain.ResourceEntity;
import org.learning.resource.mapper.SongMapper;
import org.learning.resource.repository.ResourceRepository;
import org.learning.resource.util.FileParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/resources")
@RequiredArgsConstructor
@Slf4j
public class ResourceController {

    private final ResourceRepository repository;

    private final SongFeignClient songFeignClient;

    @Value("${application.deletion.limit:200}")
    private int deletionLimit;

    @PostMapping(consumes = "audio/mpeg")
    public Map<String, Object> uploadResource(@RequestBody byte[] content) throws TikaException, IOException, SAXException {
        ResourceEntity resource = ResourceEntity.builder().content(content).build();
        resource = repository.save(resource);
        log.info("Resource is saved: {}", resource.getId());

        Metadata metadata = FileParser.getMp3Metadata(content);
        SongRequest songRequest = SongMapper.toSongRequest(resource.getId(), metadata);

        songFeignClient.saveSong(songRequest);
        log.info("Song is saved: {}", songRequest.getResourceId());

        return Map.of("id", resource.getId());
    }

    @GetMapping("/{id}")
    public byte[] getResource(@PathVariable("id") @NonNull Integer id) {
        return repository.findById(id)
                .map(ResourceEntity::getContent)
                .orElseThrow(() -> new DataNotFoundException(id));
    }

    @DeleteMapping
    public Map<String, Object> deleteResources(@RequestParam("id") @NonNull String id) {
        List<Integer> ids = Arrays.stream(id.split(","))
                .map(Integer::valueOf)
                .toList();

        if (ids.size() > deletionLimit) {
            throw new IllegalArgumentException(
                    String.format("It is not allowed to delete more than %d resources in a single request", deletionLimit));
        }

        repository.deleteAllById(ids);
        log.info("Resources are deleted: {}", ids);

        return Map.of("ids", ids);
    }

}
