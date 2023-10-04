package org.learning.microservices.song.repository;

import org.learning.microservices.song.domain.SongEntity;
import org.springframework.data.repository.CrudRepository;

public interface SongRepository extends CrudRepository<SongEntity, Integer> {
}
