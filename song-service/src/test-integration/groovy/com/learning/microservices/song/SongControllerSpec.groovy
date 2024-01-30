package com.learning.microservices.song

import jakarta.ws.rs.core.MediaType
import org.hamcrest.Matchers
import org.learning.microservices.song.SongServiceApplication
import org.learning.microservices.song.api.domain.SongRequest
import org.spockframework.spring.EnableSharedInjection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

@SpringBootTest(classes = SongServiceApplication.class)
@ActiveProfiles('test')
@AutoConfigureMockMvc
@EnableSharedInjection
@Import(TestContainerConfiguration.class)
class SongControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    def 'Song controller workflow can manage files'() {
        given:
        SongRequest request = SongRequest.builder()
                .resourceId(1)
                .name('test')
                .artist('test')
                .length(100)
                .build()

        expect:
        mockMvc.perform(MockMvcRequestBuilders.post('/v1/songs')
                .contentType(MediaType.APPLICATION_JSON)
                .content('{"resourceId": 1, "name": "test", "artist": "test", "length": 100}')
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath('$.id', Matchers.is(1)))

        and:
        mockMvc.perform(MockMvcRequestBuilders.get('/v1/songs/1')
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath('$.resourceId', Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath('$.name', Matchers.is('test')))
                .andExpect(MockMvcResultMatchers.jsonPath('$.artist', Matchers.is('test')))
                .andExpect(MockMvcResultMatchers.jsonPath('$.length', Matchers.is(100)))

        and:
        mockMvc.perform(MockMvcRequestBuilders.delete('/v1/songs?id=1')
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
    }

    def 'Song controller workflow responds accordingly if there is no song found'() {
        expect:
        mockMvc.perform(MockMvcRequestBuilders.get('/v1/songs/1')
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath('$.status', Matchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath('$.error', Matchers.is('Not Found')))
                .andExpect(MockMvcResultMatchers.jsonPath('$.message', Matchers.is('Data not found by ID: 1')))
                .andExpect(MockMvcResultMatchers.jsonPath('$.path', Matchers.is('/v1/songs/1')))
    }

}
