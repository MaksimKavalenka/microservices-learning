package org.learning.resource.repository;

import org.learning.resource.domain.ResourceEntity;
import org.springframework.data.repository.CrudRepository;

public interface ResourceRepository extends CrudRepository<ResourceEntity, Integer> {
}
