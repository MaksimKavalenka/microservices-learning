package org.learning.microservices.resource.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.learning.microservices.resource.api.message.ResourceProcessedAckMessage;
import org.learning.microservices.service.AwsS3Service;
import org.learning.microservices.storage.api.domain.StorageResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RabbitMqConfiguration {

    private final AwsS3Service awsS3Service;

    private final Map<String, StorageResponse> storages;

    @Bean
    public Consumer<ResourceProcessedAckMessage> resourceProcessedAckChannel() {
        return message -> {
            log.info("Message is received: {}", message);

            awsS3Service.moveObject(
                    message.getS3Key(),
                    storages.get("staging").getBucketName(),
                    storages.get("permanent").getBucketName());
        };
    }

}
