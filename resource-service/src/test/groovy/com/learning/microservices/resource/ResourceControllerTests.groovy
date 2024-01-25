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

import java.util.function.Consumer

@SpringBootTest(classes = ResourceServiceApplication.class)
@ActiveProfiles('test')
@AutoConfigureMockMvc
@Import(TestContainerConfiguration.class)
class ResourceControllerTests extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    S3ClientBuilder s3ClientBuilder

    def 'setup'() {
        S3Client s3 = s3ClientBuilder.build()
        s3.createBucket(
                CreateBucketRequest.builder()
                        .bucket('resources')
                        .build() as Consumer<CreateBucketRequest.Builder>)
    }

    def 'Resource upload'() {
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
    }

}
