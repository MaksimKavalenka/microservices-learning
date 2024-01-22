package org.learning.microservices.resource.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.learning.microservices.resource.configuration.properties.AwsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.*;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AwsS3Configuration {

    private final AwsProperties awsProperties;

    @Bean
    public S3ClientBuilder s3ClientBuilder() throws URISyntaxException {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(
                awsProperties.getAuth().getAccessKeyId(), awsProperties.getAuth().getSecretAccessKey());
        AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);

        S3ClientBuilder s3ClientBuilder = S3Client.builder()
                .forcePathStyle(true)
                .endpointOverride(new URI(awsProperties.getDestination().getUrl()))
                .region(Region.US_EAST_1)
                .credentialsProvider(credentialsProvider);

        checkBucket(s3ClientBuilder);

        return s3ClientBuilder;
    }

    private void checkBucket(S3ClientBuilder s3ClientBuilder) {
        String bucketName = awsProperties.getS3().getBucketName();

        if (awsProperties.getS3().isCreateIfNotExist()) {
            try (S3Client s3Client = s3ClientBuilder.build()) {
                s3Client.createBucket(CreateBucketRequest.builder()
                        .bucket(bucketName)
                        .build());
            } catch (BucketAlreadyExistsException | BucketAlreadyOwnedByYouException e) {
                log.debug("S3 bucket already exists: {}", bucketName);
            }
        } else {
            try (S3Client s3Client = s3ClientBuilder.build()) {
                s3Client.headBucket(HeadBucketRequest.builder()
                        .bucket(bucketName)
                        .build());
            } catch (NoSuchBucketException e) {
                log.error("S3 bucket does not exist: {}", bucketName);
                throw e;
            }
        }
    }

}
