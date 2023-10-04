package org.learning.microservices.song.api.client;

import org.learning.microservices.song.api.domain.SongRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "songService", url = "${application.song-service.url}")
public interface SongFeignClient {

    @RequestMapping("/api/v1/songs")
    void saveSong(SongRequest songRequest);

}
