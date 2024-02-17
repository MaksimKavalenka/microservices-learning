package org.learning.microservices.song.mapper;

import org.learning.microservices.song.api.domain.SongRequest;
import org.learning.microservices.song.api.domain.SongResponse;
import org.learning.microservices.song.entity.SongEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SongMapper {

    SongEntity toSongEntity(SongRequest songRequest);

    SongResponse toSongResponse(SongEntity songEntity);

}
