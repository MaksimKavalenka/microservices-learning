package org.learning.microservices.song.api.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    @Positive
    private Integer resourceId;

    @NotBlank
    private String name;

    @NotBlank
    private String artist;

    @NotBlank
    private String album;

    @NotNull
    @Positive
    private Integer length;

    @NotNull
    @Positive
    private Integer year;

}
