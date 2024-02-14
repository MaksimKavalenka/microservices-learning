package org.learning.microservices.processor.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.learning.microservices.processor.client.SongServiceFeignClient;
import org.learning.microservices.processor.exception.MessageProcessingException;
import org.learning.microservices.processor.mapper.SongMapper;
import org.learning.microservices.processor.util.FileParser;
import org.learning.microservices.resource.api.message.DeleteResourcesMessage;
import org.learning.microservices.resource.api.message.ProcessResourceMessage;
import org.learning.microservices.service.AwsS3Service;
import org.learning.microservices.song.api.domain.SongRequest;
import org.learning.microservices.storage.api.domain.StorageResponse;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResourceMessageListener {

    private final AwsS3Service awsS3Service;

    private final SongServiceFeignClient songService;

    private final Map<String, StorageResponse> storages;

    @Retryable
    @RabbitListener(queues = "#{'${spring.rabbitmq.queues.process}'.split(',')}")
    public void processResourceListener(Message<ProcessResourceMessage> message) throws MessageProcessingException {
        try {
            log.info("Message is received: {}", message.getPayload());

            StorageResponse stagingStorage = storages.get("staging");
            byte[] content = awsS3Service.getObjectBytes(
                    message.getPayload().getS3Key(), stagingStorage.getBucketName());
            Metadata metadata = FileParser.getMp3Metadata(content);

            SongRequest songRequest = SongMapper.toSongRequest(message.getPayload().getId(), metadata);
            songService.saveSong(songRequest);
        } catch (IOException | TikaException | SAXException e) {
            throw new MessageProcessingException();
        }
    }

    @Retryable
    @RabbitListener(queues = "#{'${spring.rabbitmq.queues.delete}'.split(',')}")
    public void deleteResourcesListener(Message<DeleteResourcesMessage> message) {
        log.info("Message is received: {}", message.getPayload());
        songService.deleteSongs(message.getPayload().getIds());
    }

}
