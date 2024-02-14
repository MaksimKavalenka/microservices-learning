package org.learning.microservices.storage.mapper;

import org.learning.microservices.storage.api.domain.StorageRequest;
import org.learning.microservices.storage.api.domain.StorageResponse;
import org.learning.microservices.storage.domain.StorageEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StorageMapper {

    StorageEntity toStorageEntity(StorageRequest storageRequest);

    StorageResponse toStorageResponse(StorageEntity storageEntity);

}
