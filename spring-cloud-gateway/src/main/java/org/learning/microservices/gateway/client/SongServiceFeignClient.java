package org.learning.microservices.gateway.client;

import org.learning.microservices.song.api.service.SongService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "song-service", url = "${infrastructure.song-service.url:}")
public interface SongServiceFeignClient extends SongService {
}
