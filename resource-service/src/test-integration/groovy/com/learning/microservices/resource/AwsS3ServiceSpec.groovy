package com.learning.microservices.resource

import org.learning.microservices.resource.ResourceServiceApplication
import org.learning.microservices.service.AwsS3Service
import org.spockframework.spring.EnableSharedInjection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.S3ClientBuilder
import software.amazon.awssdk.services.s3.model.CreateBucketRequest
import software.amazon.awssdk.services.s3.model.NoSuchKeyException
import spock.lang.Shared
import spock.lang.Specification

import java.nio.charset.StandardCharsets

@SpringBootTest(classes = ResourceServiceApplication.class)
@ActiveProfiles('test')
@AutoConfigureMockMvc
@EnableSharedInjection
@Import(TestContainerConfiguration.class)
class AwsS3ServiceSpec extends Specification {

    @Autowired
    @Shared
    S3ClientBuilder s3ClientBuilder

    @Autowired
    AwsS3Service awsS3Service

    def setupSpec() {
        S3Client s3 = s3ClientBuilder.build()
        s3.createBucket(
                CreateBucketRequest.builder()
                        .bucket('staging')
                        .build())
    }

    def 'S3 LocalStack can manage files'() {
        given:
        String s3Key = 'test_file'
        String bucket = 'staging'
        ClassLoader classLoader = getClass().getClassLoader()
        File file = new File(classLoader.getResource("files/test_integration_file.txt").getFile())

        when: 'Save a file into S3'
        awsS3Service.putObject(s3Key, bucket, file.getBytes())

        and: 'Get its content from S3'
        byte[] content = awsS3Service.getObjectBytes(s3Key, bucket)

        then: 'The contents are identical'
        file.getText() == new String(content, StandardCharsets.UTF_8)

        when: 'Delete the file and try to get its content'
        awsS3Service.deleteObjects(Collections.singletonList(s3Key), bucket)
        awsS3Service.getObjectBytes(s3Key, bucket)

        then: 'The exception is thrown'
        thrown NoSuchKeyException
    }

}
