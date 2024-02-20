package org.learning.microservices.song.repository;

import org.learning.microservices.song.entity.SongEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SongRepository extends CrudRepository<SongEntity, Integer> {

    @Query("SELECT song.resourceId FROM SongEntity song WHERE song.resourceId IN (:ids)")
    List<Integer> getExistingIds(List<Integer> ids);

    @Modifying
    @Transactional
    @Query("DELETE FROM SongEntity song WHERE song.resourceId IN (:ids)")
    void deleteAllById(List<Integer> ids);

}
