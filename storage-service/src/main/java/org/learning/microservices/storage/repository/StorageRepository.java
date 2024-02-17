package org.learning.microservices.storage.repository;

import org.learning.microservices.storage.domain.StorageEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StorageRepository extends CrudRepository<StorageEntity, Integer> {

    boolean existsByStorageType(String storageType);

    @Query("SELECT storage.id FROM StorageEntity storage WHERE storage.id IN (:ids)")
    List<Integer> getExistingIds(List<Integer> ids);

    @Modifying
    @Transactional
    @Query("DELETE FROM StorageEntity storage WHERE storage.id IN (:ids)")
    void deleteAllById(List<Integer> ids);

}
