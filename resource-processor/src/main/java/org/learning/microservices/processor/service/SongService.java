package org.learning.microservices.processor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.learning.microservices.processor.client.SongServiceFeignClient;
import org.learning.microservices.song.api.domain.SongRequest;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SongService {

    private final SongServiceFeignClient songService;

    @Retryable
    public void saveSong(SongRequest songRequest) {
        songService.saveSong(songRequest);
        log.info("Song is saved: {}", songRequest.getResourceId());
    }

    @Retryable
    public void deleteSongs(String ids) {
        songService.deleteSongs(ids);
        log.info("Songs are deleted: {}", ids);
    }

}
