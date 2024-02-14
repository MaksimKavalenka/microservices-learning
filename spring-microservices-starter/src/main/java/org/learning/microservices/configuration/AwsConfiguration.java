package org.learning.microservices.configuration;

import org.learning.microservices.configuration.properties.AwsProperties;
import org.learning.microservices.service.AwsS3Service;
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

    @ConditionalOnProperty("aws.s3.enabled")
    public static class AwsS3Configuration {

        @Bean
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

        @Bean
        public AwsS3Service awsS3Service(S3ClientBuilder s3ClientBuilder) {
            return new AwsS3Service(s3ClientBuilder);
        }

    }

}
