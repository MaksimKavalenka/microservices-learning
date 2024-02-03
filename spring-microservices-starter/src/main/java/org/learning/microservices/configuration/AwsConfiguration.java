package org.learning.microservices.configuration;

import org.learning.microservices.configuration.properties.AwsProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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

@Configuration
public class AwsConfiguration {

    @Bean
    @ConditionalOnProperty("aws.s3.bucket-name")
    public S3ClientBuilder s3ClientBuilder(AwsProperties awsProperties) throws URISyntaxException {
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
