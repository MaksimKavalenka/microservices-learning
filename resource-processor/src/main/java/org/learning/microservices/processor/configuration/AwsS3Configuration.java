package org.learning.microservices.processor.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.learning.microservices.processor.configuration.properties.AwsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

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

        return S3Client.builder()
                .forcePathStyle(true)
                .endpointOverride(new URI(awsProperties.getDestination().getUrl()))
                .region(Region.US_EAST_1)
                .credentialsProvider(credentialsProvider);
    }

}
