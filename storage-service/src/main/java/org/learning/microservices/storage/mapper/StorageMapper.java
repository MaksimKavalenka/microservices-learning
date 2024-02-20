package org.learning.microservices.storage.mapper;

import org.learning.microservices.storage.api.domain.StorageRequest;
import org.learning.microservices.storage.api.domain.StorageResponse;
import org.learning.microservices.storage.entity.StorageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StorageMapper {

    @Mapping(target = "id", ignore = true)
    StorageEntity toStorageEntity(StorageRequest storageRequest);

    StorageResponse toStorageResponse(StorageEntity storageEntity);

}
