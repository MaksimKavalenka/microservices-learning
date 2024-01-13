package org.learning.microservices.resource.service;

import lombok.RequiredArgsConstructor;
import org.learning.microservices.resource.configuration.properties.AwsProperties;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

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

    public void putObject(String key, Path path) {
        try (S3Client s3 = s3ClientBuilder.build()) {
            s3.putObject(PutObjectRequest.builder()
                    .bucket(awsProperties.getS3().getBucketName())
                    .key(key)
                    .build(), path);
        }
    }

    public void deleteObjects(List<String> keys) {
        List<ObjectIdentifier> identifiers = keys.stream()
                .map(key -> ObjectIdentifier.builder().key(key).build())
                .toList();

        try (S3Client s3 = s3ClientBuilder.build()) {
            s3.deleteObjects(DeleteObjectsRequest.builder()
                    .bucket(awsProperties.getS3().getBucketName())
                    .delete(Delete.builder().objects(identifiers).build())
                    .build());
        }
    }

}
