package org.learning.microservices.song.api.service;

import org.learning.microservices.song.api.domain.SongRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface SongService {

    @PostMapping("/api/v1/songs")
    Map<String, Object> saveSong(SongRequest songRequest);

    @DeleteMapping("/api/v1/songs")
    Map<String, Object> deleteSongs(@RequestParam("id") String id);

}
