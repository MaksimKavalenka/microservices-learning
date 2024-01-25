package org.learning.microservices.processor.service;

import lombok.RequiredArgsConstructor;
import org.learning.microservices.configuration.properties.AwsProperties;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AwsS3Service {

    private final AwsProperties awsProperties;

    private final S3ClientBuilder s3ClientBuilder;

    public byte[] getObjectBytes(String key) throws IOException {
        try (S3Client s3 = s3ClientBuilder.build()) {
            return s3.getObject(GetObjectRequest.builder()
                    .bucket(awsProperties.getS3().getBucketName())
                    .key(key)
                    .build()).readAllBytes();
        }
    }

}
