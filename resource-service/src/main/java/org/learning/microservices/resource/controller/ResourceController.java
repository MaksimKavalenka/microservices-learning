package org.learning.microservices.resource.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.learning.microservices.exception.DataNotFoundException;
import org.learning.microservices.resource.domain.ResourceEntity;
import org.learning.microservices.resource.mapper.SongMapper;
import org.learning.microservices.resource.repository.ResourceRepository;
import org.learning.microservices.resource.util.FileParser;
import org.learning.microservices.song.api.client.SongFeignClient;
import org.learning.microservices.song.api.domain.SongRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
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
@Validated
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
    public byte[] getResource(@PathVariable("id") @Positive Integer id) {
        return repository.findById(id)
                .map(ResourceEntity::getContent)
                .orElseThrow(() -> new DataNotFoundException(id));
    }

    @DeleteMapping
    public Map<String, Object> deleteResources(@RequestParam("id") @NotBlank String id) {
        List<Integer> ids = Arrays.stream(id.split(","))
                .map(Integer::valueOf)
                .toList();

        if (ids.size() > deletionLimit) {
            throw new IllegalArgumentException(
                    String.format("It is not allowed to delete more than %d resources in a single request", deletionLimit));
        }

        repository.deleteAllById(ids);
        log.info("Resources are deleted: {}", ids);

        songFeignClient.deleteSongs(id);
        log.info("Songs are deleted: {}", ids);

        return Map.of("ids", ids);
    }

}
