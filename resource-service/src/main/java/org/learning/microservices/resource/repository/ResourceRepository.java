package org.learning.microservices.resource.repository;

import org.learning.microservices.resource.domain.ResourceEntity;
import org.springframework.data.repository.CrudRepository;

public interface ResourceRepository extends CrudRepository<ResourceEntity, Integer> {
}
