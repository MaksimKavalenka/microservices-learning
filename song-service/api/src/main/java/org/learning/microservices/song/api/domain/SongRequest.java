package org.learning.microservices.song.api.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongRequest {

    @NotNull
    private Integer resourceId;

    private String name;

    private String artist;

    private String album;

    private String length;

    private Integer year;

}
