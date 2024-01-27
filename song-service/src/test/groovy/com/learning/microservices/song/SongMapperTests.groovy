package com.learning.microservices.song

import org.learning.microservices.song.api.domain.SongRequest
import org.learning.microservices.song.domain.SongEntity
import org.learning.microservices.song.mapper.SongMapper
import org.learning.microservices.song.mapper.SongMapperImpl
import spock.lang.Specification

class SongMapperTests extends Specification {

    SongMapper songMapper = new SongMapperImpl()

    def 'Song mapper converts a request to entity correctly'() {
        given:
        SongRequest request = SongRequest.builder()
                .resourceId(1)
                .name('name')
                .artist('artist')
                .genre('genre')
                .album('album')
                .length(100)
                .year(2000)
                .build()

        when:
        SongEntity entity = songMapper.toSongEntity(request)

        then:
        entity.getResourceId() == request.getResourceId()
        entity.getName() == request.getName()
        entity.getGenre() == request.getGenre()
        entity.getAlbum() == request.getAlbum()
        entity.getLength() == request.getLength()
        entity.getYear() == request.getYear()
    }

}
