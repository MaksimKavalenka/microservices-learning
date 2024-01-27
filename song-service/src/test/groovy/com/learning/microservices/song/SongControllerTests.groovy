package com.learning.microservices.song

import org.learning.microservices.song.api.domain.SongRequest
import org.learning.microservices.song.controller.SongController
import org.learning.microservices.song.domain.SongEntity
import org.learning.microservices.song.mapper.SongMapper
import org.learning.microservices.song.repository.SongRepository
import spock.lang.Shared
import spock.lang.Specification

import static org.mockito.Mockito.*

class SongControllerTests extends Specification {

    @Shared
    SongMapper songMapper

    @Shared
    SongRepository songRepository

    @Shared
    SongController songController

    def setup() {
        songMapper = mock(SongMapper.class)
        songRepository = mock(SongRepository.class)
        songController = new SongController(songMapper, songRepository)
        songController.deletionLimit = 200
    }

    def 'Song controller saves a song'() {
        given:
        SongRequest request = SongRequest.builder()
                .resourceId(1)
                .name('test')
                .artist('test')
                .length(100)
                .build()

        SongEntity entity = SongEntity.builder()
                .resourceId(1)
                .build()

        when:
        when(songMapper.toSongEntity(request)).thenReturn(entity)
        when(songRepository.save(entity)).thenReturn(entity)

        and:
        Map<String, Object> response = songController.saveSong(request)

        then:
        response.get('id') == 1

        and:
        verify(songMapper, times(1)).toSongEntity(request) || true
        verify(songRepository, times(1)).save(entity) || true
    }

    def 'Song controller returns a song'() {
        given:
        SongEntity entity = SongEntity.builder()
                .resourceId(1)
                .build()

        when:
        when(songRepository.findById(1)).thenReturn(Optional.of(entity))

        and:
        SongEntity response = songController.getSong(1)

        then:
        response.getResourceId() == 1

        and:
        verify(songRepository, times(1)).findById(1) || true
    }

    def 'Song controller deletes a song'() {
        given:
        List<Integer> ids = [1, 2, 3]

        when:
        when(songRepository.getExistingIds(ids)).thenReturn(ids)
        doNothing().when(songRepository).deleteAllById(ids)

        and:
        Map<String, Object> response = songController.deleteSongs('1,2,3')

        then:
        response.get('ids') == [1, 2, 3]

        and:
        verify(songRepository, times(1)).getExistingIds(ids) || true
        verify(songRepository, times(1)).deleteAllById(ids) || true
    }

    def 'Song controller throws the exception when exceeding the deletion limit'() {
        given:
        songController.deletionLimit = 1

        and:
        List<Integer> ids = [1, 2, 3]

        when:
        songController.deleteSongs('1,2,3')

        then:
        thrown IllegalArgumentException

        and:
        verify(songRepository, never()).getExistingIds(ids) || true
        verify(songRepository, never()).deleteAllById(ids) || true
    }

}
