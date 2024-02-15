package org.learning.microservices.service;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class AwsS3Service {

    private final S3ClientBuilder s3ClientBuilder;

    public byte[] getObjectBytes(String key, String bucket) throws IOException {
        try (S3Client s3 = s3ClientBuilder.build()) {
            return s3.getObject(GetObjectRequest.builder()
                    .key(key)
                    .bucket(bucket)
                    .build()).readAllBytes();
        }
    }

    public void putObject(String key, String bucket, byte[] content) {
        try (S3Client s3 = s3ClientBuilder.build()) {
            s3.putObject(PutObjectRequest.builder()
                    .key(key)
                    .bucket(bucket)
                    .build(), RequestBody.fromBytes(content));
        }
    }

    public void moveObject(String key, String sourceBucket, String destinationBucket) {
        try (S3Client s3 = s3ClientBuilder.build()) {
            s3.copyObject(CopyObjectRequest.builder()
                    .sourceKey(key)
                    .sourceBucket(sourceBucket)
                    .destinationKey(key)
                    .destinationBucket(destinationBucket)
                    .build());

            s3.deleteObject(DeleteObjectRequest.builder()
                    .key(key)
                    .bucket(sourceBucket)
                    .build());
        }
    }

    public void deleteObjects(List<String> keys, String bucket) {
        List<ObjectIdentifier> identifiers = keys.stream()
                .map(key -> ObjectIdentifier.builder().key(key).build())
                .toList();

        try (S3Client s3 = s3ClientBuilder.build()) {
            s3.deleteObjects(DeleteObjectsRequest.builder()
                    .delete(Delete.builder().objects(identifiers).build())
                    .bucket(bucket)
                    .build());
        }
    }

}
