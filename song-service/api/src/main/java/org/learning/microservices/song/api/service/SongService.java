package org.learning.microservices.song.api.service;

import org.learning.microservices.song.api.domain.SongRequest;
import org.learning.microservices.song.api.domain.SongResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

public interface SongService {

    @PostMapping("/api/v1/songs")
    Map<String, Object> saveSong(SongRequest songRequest);

    @GetMapping("/api/v1/songs/{id}")
    SongResponse getSong(@PathVariable("id") Integer id);

    @DeleteMapping("/api/v1/songs")
    Map<String, Object> deleteSongs(@RequestParam("id") String id);

}
