package org.learning.microservices.processor.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.learning.microservices.processor.exception.MessageProcessingException;
import org.learning.microservices.processor.mapper.SongMapper;
import org.learning.microservices.processor.util.FileParser;
import org.learning.microservices.resource.api.message.DeleteResourcesMessage;
import org.learning.microservices.resource.api.message.ProcessResourceMessage;
import org.learning.microservices.resource.api.message.ResourceProcessedAckMessage;
import org.learning.microservices.service.AwsS3Service;
import org.learning.microservices.song.api.domain.SongRequest;
import org.learning.microservices.song.api.service.SongService;
import org.learning.microservices.storage.api.domain.StorageResponse;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RabbitMqConfiguration {

    private final AwsS3Service awsS3Service;

    private final SongService songService;

    private final StreamBridge streamBridge;

    private final Map<String, StorageResponse> storages;

    @Bean
    public Consumer<ProcessResourceMessage> resourceProcessChannel() {
        return message -> {
            try {
                log.info("Message is received: {}", message);

                StorageResponse stagingStorage = storages.get("staging");
                byte[] content = awsS3Service.getObjectBytes(
                        message.getS3Key(), stagingStorage.getBucketName());
                Metadata metadata = FileParser.getMp3Metadata(content);

                SongRequest songRequest = SongMapper.toSongRequest(message.getId(), metadata);
                songService.saveSong(songRequest);

                ResourceProcessedAckMessage ackMessage = ResourceProcessedAckMessage.builder()
                        .id(message.getId())
                        .s3Key(message.getS3Key())
                        .build();

                streamBridge.send("resourceProcessedAckChannel", ackMessage);
                log.info("The message is sent: {}", ackMessage);
            } catch (IOException | TikaException | SAXException e) {
                throw new MessageProcessingException();
            }
        };
    }

    @Bean
    public Consumer<DeleteResourcesMessage> resourceDeletionChannel() {
        return message -> {
            log.info("Message is received: {}", message);
            songService.deleteSongs(message.getIds());
        };
    }

}
