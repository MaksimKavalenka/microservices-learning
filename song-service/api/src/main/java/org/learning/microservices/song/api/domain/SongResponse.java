package org.learning.microservices.song.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongResponse {

    private Integer resourceId;

    private String name;

    private String artist;

    private String genre;

    private String album;

    private Integer length;

    private Integer year;

}
