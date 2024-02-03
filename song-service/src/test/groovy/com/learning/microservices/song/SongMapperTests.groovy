package com.learning.microservices.song

import org.learning.microservices.song.api.domain.SongRequest
import org.learning.microservices.song.api.domain.SongResponse
import org.learning.microservices.song.domain.SongEntity
import org.learning.microservices.song.mapper.SongMapper
import org.learning.microservices.song.mapper.SongMapperImpl
import spock.lang.Specification

class SongMapperTests extends Specification {

    SongMapper songMapper = new SongMapperImpl()

    def 'Song mapper converts a request to an entity correctly'() {
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

    def 'Song mapper converts an entity to a response correctly'() {
        given:
        SongEntity song = SongEntity.builder()
                .resourceId(1)
                .name('name')
                .artist('artist')
                .genre('genre')
                .album('album')
                .length(100)
                .year(2000)
                .build()

        when:
        SongResponse response = songMapper.toSongResponse(song)

        then:
        response.getResourceId() == song.getResourceId()
        response.getName() == song.getName()
        response.getGenre() == song.getGenre()
        response.getAlbum() == song.getAlbum()
        response.getLength() == song.getLength()
        response.getYear() == song.getYear()
    }

}
