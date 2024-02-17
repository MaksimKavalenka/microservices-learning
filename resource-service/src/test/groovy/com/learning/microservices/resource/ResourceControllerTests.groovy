package com.learning.microservices.resource

import org.learning.microservices.resource.configuration.properties.RabbitBindingProperties
import org.learning.microservices.resource.controller.ResourceController
import org.learning.microservices.resource.entity.ResourceEntity
import org.learning.microservices.resource.repository.ResourceRepository
import org.learning.microservices.service.AwsS3Service
import org.springframework.amqp.rabbit.core.RabbitTemplate
import spock.lang.Shared
import spock.lang.Specification

import static org.learning.microservices.resource.configuration.properties.RabbitBindingProperties.*
import static org.mockito.ArgumentMatchers.*
import static org.mockito.Mockito.*

class ResourceControllerTests extends Specification {

    @Shared
    AwsS3Service awsS3Service

    @Shared
    RabbitBindingProperties rabbitBindingProperties

    @Shared
    RabbitTemplate rabbitTemplate

    @Shared
    ResourceRepository resourceRepository

    @Shared
    ResourceController resourceController

    def setup() {
        awsS3Service = mock(AwsS3Service.class)
        rabbitBindingProperties = mock(RabbitBindingProperties.class)
        rabbitTemplate = mock(RabbitTemplate.class)
        resourceRepository = mock(ResourceRepository.class)
        resourceController = new ResourceController(awsS3Service, rabbitBindingProperties, rabbitTemplate, resourceRepository)
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

        and:
        BindingProperties bindingProperties = new BindingProperties()
        bindingProperties.setSource('test')
        bindingProperties.setRoutingKey('test')

        when:
        doNothing().when(awsS3Service).putObject(anyString(), any())
        when(resourceRepository.save(any())).thenReturn(entity)
        when(rabbitBindingProperties.getBindings()).thenReturn(Map.of(PROCESS_BINDING_KEY, bindingProperties))
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), any())

        and:
        Map<String, Object> response = resourceController.uploadResource(file.getBytes())

        then:
        response.get('id') == 1

        and:
        verify(awsS3Service, times(1)).putObject(anyString(), any()) || true
        verify(resourceRepository, times(1)).save(any()) || true
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), any()) || true
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
        when(awsS3Service.getObjectBytes(entity.getS3Key())).thenReturn(file.getBytes())

        and:
        byte[] content = resourceController.getResource(1)

        then:
        file.getText() == new String(content)

        and:
        verify(resourceRepository, times(1)).findById(entity.getId()) || true
        verify(awsS3Service, times(1)).getObjectBytes(entity.getS3Key()) || true
    }

    def 'Resource controller deletes a content'() {
        given:
        List<Integer> ids = [1, 2, 3]
        List<String> s3Keys = ['key1', 'key2', 'key3']

        and:
        BindingProperties bindingProperties = new BindingProperties()
        bindingProperties.setSource('test')
        bindingProperties.setRoutingKey('test')

        when:
        when(resourceRepository.getS3Keys(ids)).thenReturn(s3Keys)
        doNothing().when(awsS3Service).deleteObjects(s3Keys)
        doNothing().when(resourceRepository).deleteAllById(ids)
        when(rabbitBindingProperties.getBindings()).thenReturn(Map.of(DELETE_BINDING_KEY, bindingProperties))
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), any())

        and:
        Map<String, Object> response = resourceController.deleteResources('1,2,3')

        then:
        response.get('ids') == ids

        and:
        verify(resourceRepository, times(1)).getS3Keys(ids) || true
        verify(awsS3Service, times(1)).deleteObjects(s3Keys) || true
        verify(resourceRepository, times(1)).deleteAllById(ids) || true
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), any()) || true
    }

    def 'Resource controller throws the exception when exceeding the deletion limit'() {
        given:
        resourceController.deletionLimit = 1

        and:
        BindingProperties bindingProperties = new BindingProperties()
        bindingProperties.setSource('test')
        bindingProperties.setRoutingKey('test')

        when:
        resourceController.deleteResources('1,2,3')

        then:
        thrown IllegalArgumentException

        and:
        verify(resourceRepository, never()).getS3Keys(anyList()) || true
        verify(awsS3Service, never()).deleteObjects(anyList()) || true
        verify(resourceRepository, never()).deleteAllById(anyList()) || true
        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString(), any()) || true
    }

}
