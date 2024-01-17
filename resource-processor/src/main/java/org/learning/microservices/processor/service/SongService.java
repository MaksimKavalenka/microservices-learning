package org.learning.microservices.processor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.learning.microservices.processor.client.SongServiceFeignClient;
import org.learning.microservices.song.api.domain.SongRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SongService {

    private final SongServiceFeignClient songService;

    public void saveSong(SongRequest songRequest) {
        songService.saveSong(songRequest);
        log.info("Song is saved: {}", songRequest.getResourceId());
    }

}
