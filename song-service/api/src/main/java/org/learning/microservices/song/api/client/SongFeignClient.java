package org.learning.microservices.song.api.client;

import org.learning.microservices.song.api.domain.SongRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "songService", url = "${infrastructure.song-service.url}")
public interface SongFeignClient {

    @PostMapping("/api/v1/songs")
    Map<String, Object> saveSong(SongRequest songRequest);

    @DeleteMapping("/api/v1/songs")
    Map<String, Object> deleteSongs(@RequestParam("id") String id);

}
