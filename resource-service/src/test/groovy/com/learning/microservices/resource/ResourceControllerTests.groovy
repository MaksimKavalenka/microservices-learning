package com.learning.microservices.resource

import org.learning.microservices.resource.controller.ResourceController
import org.learning.microservices.resource.entity.ResourceEntity
import org.learning.microservices.resource.repository.ResourceRepository
import org.learning.microservices.service.AwsS3Service
import org.learning.microservices.storage.api.domain.StorageResponse
import org.springframework.cloud.stream.function.StreamBridge
import spock.lang.Shared
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.*
import static org.mockito.Mockito.*

class ResourceControllerTests extends Specification {

    @Shared
    AwsS3Service awsS3Service

    @Shared
    StreamBridge streamBridge

    @Shared
    ResourceRepository resourceRepository

    @Shared
    ResourceController resourceController

    @Shared
    Map<String, StorageResponse> storages = Map.of(
            "staging",
            StorageResponse.builder()
                    .storageType("staging")
                    .bucketName("staging")
                    .path("/files")
                    .build(),

            "permanent",
            StorageResponse.builder()
                    .storageType("permanent")
                    .bucketName("permanent")
                    .path("/files")
                    .build())

    def setup() {
        awsS3Service = mock(AwsS3Service.class)
        streamBridge = mock(StreamBridge.class)
        resourceRepository = mock(ResourceRepository.class)
        resourceController = new ResourceController(
                awsS3Service, streamBridge, resourceRepository, storages)
        resourceController.deletionLimit = 200
    }

    def 'Resource controller uploads a content'() {
        given:
        ResourceEntity entity = ResourceEntity.builder()
                .id(1)
                .s3Key('s3TestKey')
                .build()

        and:
        ClassLoader classLoader = getClass().getClassLoader()
        File file = new File(classLoader.getResource("files/test_file.txt").getFile())

        when:
        doNothing().when(awsS3Service).putObject(anyString(), matches("staging"), any())
        when(resourceRepository.save(any())).thenReturn(entity)
        when(streamBridge.send(eq("resourceProcessChannel"), any())).thenReturn(true)

        and:
        Map<String, Object> response = resourceController.uploadResource(file.getBytes())

        then:
        response.get('id') == 1

        and:
        verify(awsS3Service, times(1)).putObject(anyString(), matches("staging"), any()) || true
        verify(resourceRepository, times(1)).save(any()) || true
        verify(streamBridge, times(1)).send(eq("resourceProcessChannel"), any()) || true
    }

    def 'Resource controller returns a content'() {
        given:
        ResourceEntity entity = ResourceEntity.builder()
                .id(1)
                .s3Key('s3TestKey')
                .build()

        and:
        ClassLoader classLoader = getClass().getClassLoader()
        File file = new File(classLoader.getResource("files/test_file.txt").getFile())

        when:
        when(resourceRepository.findById(1)).thenReturn(Optional.of(entity))
        when(awsS3Service.getObjectBytes(entity.getS3Key(), "permanent")).thenReturn(file.getBytes())

        and:
        byte[] content = resourceController.getResource(1)

        then:
        file.getText() == new String(content)

        and:
        verify(resourceRepository, times(1)).findById(entity.getId()) || true
        verify(awsS3Service, times(1)).getObjectBytes(entity.getS3Key(), "permanent") || true
    }

    def 'Resource controller deletes a content'() {
        given:
        List<Integer> ids = [1, 2, 3]
        List<String> s3Keys = ['key1', 'key2', 'key3']

        when:
        when(resourceRepository.getS3Keys(ids)).thenReturn(s3Keys)
        doNothing().when(awsS3Service).deleteObjects(s3Keys, "permanent")
        doNothing().when(resourceRepository).deleteAllById(ids)
        when(streamBridge.send(eq("resourceDeletionChannel"), any())).thenReturn(true)

        and:
        Map<String, Object> response = resourceController.deleteResources('1,2,3')

        then:
        response.get('ids') == ids

        and:
        verify(resourceRepository, times(1)).getS3Keys(ids) || true
        verify(awsS3Service, times(1)).deleteObjects(s3Keys, "permanent") || true
        verify(resourceRepository, times(1)).deleteAllById(ids) || true
        verify(streamBridge, times(1)).send(eq("resourceDeletionChannel"), any()) || true
    }

    def 'Resource controller throws the exception when exceeding the deletion limit'() {
        given:
        resourceController.deletionLimit = 1

        when:
        resourceController.deleteResources('1,2,3')

        then:
        thrown IllegalArgumentException

        and:
        verify(resourceRepository, never()).getS3Keys(anyList()) || true
        verify(awsS3Service, never()).deleteObjects(anyList(), matches("permanent")) || true
        verify(resourceRepository, never()).deleteAllById(anyList()) || true
        verify(streamBridge, never()).send(eq("resourceDeletionChannel"), any()) || true
    }

}
