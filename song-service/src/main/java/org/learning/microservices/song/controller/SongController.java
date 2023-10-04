package org.learning.microservices.song.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.learning.microservices.song.api.domain.SongRequest;
import org.learning.microservices.song.domain.SongEntity;
import org.learning.microservices.song.mapper.SongMapper;
import org.learning.microservices.song.repository.SongRepository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/v1/songs")
@RequiredArgsConstructor
@Slf4j
public class SongController {

    private final SongMapper mapper;

    private final SongRepository repository;

    @PostMapping
    public Map<String, Object> saveSong(@RequestBody @Validated SongRequest songRequest) {
        SongEntity song = mapper.toSongEntity(songRequest);
        repository.save(song);
        log.info("Song is saved: {}", song.getResourceId());
        return Map.of("id", song.getResourceId());
    }

}
