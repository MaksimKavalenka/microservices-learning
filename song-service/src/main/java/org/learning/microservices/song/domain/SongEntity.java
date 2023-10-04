package org.learning.microservices.song.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "songs")
public class SongEntity {

    @Id
    private Integer resourceId;

    private String name;

    private String artist;

    private String album;

    private Integer length;

    private Integer year;

}
