package org.learning.microservices.resource.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.learning.microservices.resource.api.message.ResourceProcessedAckMessage;
import org.learning.microservices.service.AwsS3Service;
import org.learning.microservices.storage.api.domain.StorageResponse;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResourceMessageListener {

    private final AwsS3Service awsS3Service;

    private final Map<String, StorageResponse> storages;

    @Retryable
    @RabbitListener(queues = "#{'${spring.rabbitmq.queues.processed-ack}'.split(',')}")
    public void resourceProcessedAckListener(Message<ResourceProcessedAckMessage> message) {
        log.info("Message is received: {}", message.getPayload());

        awsS3Service.moveObject(
                message.getPayload().getS3Key(),
                storages.get("staging").getBucketName(),
                storages.get("permanent").getBucketName());
    }

}
