package com.learning.microservices.resource

import jakarta.ws.rs.core.MediaType
import org.hamcrest.Matchers
import org.learning.microservices.resource.ResourceServiceApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.S3ClientBuilder
import software.amazon.awssdk.services.s3.model.CreateBucketRequest
import spock.lang.Specification

@SpringBootTest(classes = ResourceServiceApplication.class)
@ActiveProfiles('test')
@AutoConfigureMockMvc
@Import(TestContainerConfiguration.class)
class ResourceControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    S3ClientBuilder s3ClientBuilder

    def 'setup'() {
        S3Client s3 = s3ClientBuilder.build()
        s3.createBucket(
                CreateBucketRequest.builder()
                        .bucket('resources')
                        .build())
    }

    def 'Resource upload workflow can manage files'() {
        given:
        ClassLoader classLoader = getClass().getClassLoader()
        File file = new File(classLoader.getResource("files/test_file.txt").getFile())

        expect:
        mockMvc.perform(MockMvcRequestBuilders.post('/v1/resources')
                .contentType('audio/mpeg')
                .content(file.getBytes())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath('$.id', Matchers.is(1)))

        and:
        mockMvc.perform(MockMvcRequestBuilders.get('/v1/resources/1')
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(file.getText()))

        and:
        mockMvc.perform(MockMvcRequestBuilders.delete('/v1/resources?id=1')
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
    }

    def 'Resource upload workflow responds accordingly if there is no file found'() {
        expect:
        mockMvc.perform(MockMvcRequestBuilders.get('/v1/resources/1')
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath('$.status', Matchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath('$.error', Matchers.is('Not Found')))
                .andExpect(MockMvcResultMatchers.jsonPath('$.message', Matchers.is('Data not found by ID: 1')))
                .andExpect(MockMvcResultMatchers.jsonPath('$.path', Matchers.is('/v1/resources/1')))
    }

}
