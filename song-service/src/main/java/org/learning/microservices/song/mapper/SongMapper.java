package org.learning.microservices.song.mapper;

import org.learning.microservices.song.api.domain.SongRequest;
import org.learning.microservices.song.domain.SongEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SongMapper {

    SongMapper INSTANCE = Mappers.getMapper(SongMapper.class);

    SongEntity toSongEntity(SongRequest songRequest);

}
