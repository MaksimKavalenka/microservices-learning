package org.learning.microservices.song.mapper;

import org.learning.microservices.song.api.domain.SongRequest;
import org.learning.microservices.song.domain.SongEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SongMapper {

    SongEntity toSongEntity(SongRequest songRequest);

}
