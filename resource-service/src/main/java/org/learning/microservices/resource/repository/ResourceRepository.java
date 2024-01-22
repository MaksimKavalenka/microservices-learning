package org.learning.microservices.resource.repository;

import org.learning.microservices.resource.domain.ResourceEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ResourceRepository extends CrudRepository<ResourceEntity, Integer> {

    @Query("SELECT resource.s3Key FROM ResourceEntity resource WHERE resource.id IN (:ids)")
    List<String> getS3Keys(List<Integer> ids);

    @Modifying
    @Transactional
    @Query("DELETE FROM ResourceEntity resource WHERE resource.id IN (:ids)")
    void deleteAllById(List<Integer> ids);

}
