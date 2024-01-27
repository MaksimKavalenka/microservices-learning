package com.learning.microservices.song

import jakarta.ws.rs.core.MediaType
import org.hamcrest.Matchers
import org.learning.microservices.song.SongServiceApplication
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
@Import(TestContainerConfiguration.class)
class SongServiceApplicationSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    def 'Health check'() {
        expect:
        mockMvc.perform(MockMvcRequestBuilders.get('/actuator/health')
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath('$.status', Matchers.is('UP')))
    }

}
