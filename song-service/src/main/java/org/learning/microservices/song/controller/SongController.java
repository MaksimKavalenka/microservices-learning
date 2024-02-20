package org.learning.microservices.song.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.learning.microservices.exception.DataNotFoundException;
import org.learning.microservices.song.api.domain.SongRequest;
import org.learning.microservices.song.api.domain.SongResponse;
import org.learning.microservices.song.entity.SongEntity;
import org.learning.microservices.song.mapper.SongMapper;
import org.learning.microservices.song.repository.SongRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/songs")
@RequiredArgsConstructor
@Slf4j
@Validated
public class SongController {

    private final SongMapper mapper;

    private final SongRepository repository;

    @Value("${application.deletion.limit:200}")
    private int deletionLimit;

    @PostMapping
    public Map<String, Object> saveSong(@RequestBody @Valid SongRequest songRequest) {
        SongEntity song = mapper.toSongEntity(songRequest);
        repository.save(song);
        log.info("Song is saved: {}", song.getResourceId());

        return Map.of("id", song.getResourceId());
    }

    @GetMapping("/{id}")
    public SongResponse getSong(@PathVariable("id") @Positive Integer id) {
        SongEntity song = repository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(id));
        return mapper.toSongResponse(song);
    }

    @DeleteMapping
    public Map<String, Object> deleteSongs(@RequestParam("id") @NotBlank String id) {
        List<Integer> ids = Arrays.stream(id.split(","))
                .map(Integer::valueOf)
                .toList();

        if (ids.size() > deletionLimit) {
            throw new IllegalArgumentException(
                    String.format("It is not allowed to delete more than %d songs in a single request", deletionLimit));
        }

        ids = repository.getExistingIds(ids);
        repository.deleteAllById(ids);
        log.info("Songs are deleted: {}", ids);

        return Map.of("ids", ids);
    }

}
