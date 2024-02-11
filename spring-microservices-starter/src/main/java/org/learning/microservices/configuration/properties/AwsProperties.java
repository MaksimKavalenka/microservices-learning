package org.learning.microservices.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("aws")
public class AwsProperties {

    private AuthenticationProperties auth;

    private DestinationProperties destination;

    private S3Properties s3;

    @Data
    public static class AuthenticationProperties {

        private String accessKeyId;

        private String secretAccessKey;

    }

    @Data
    public static class DestinationProperties {

        private String host;

        private int port;

        private String url;

    }

    @Data
    public static class S3Properties {

        private String bucketName;

    }

}
