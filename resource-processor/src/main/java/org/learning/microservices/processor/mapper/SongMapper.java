package org.learning.microservices.processor.mapper;

import lombok.experimental.UtilityClass;
import org.apache.tika.metadata.Metadata;
import org.learning.microservices.song.api.domain.SongRequest;

@UtilityClass
public class SongMapper {

    public static SongRequest toSongRequest(Integer resourceId, Metadata metadata) {
        return SongRequest.builder()
                .resourceId(resourceId)
                .name(metadata.get("dc:title"))
                .artist(metadata.get("xmpDM:artist"))
                .genre(metadata.get("xmpDM:genre"))
                .album(metadata.get("xmpDM:album"))
                .length((int) Math.ceil(Double.parseDouble(metadata.get("xmpDM:duration"))))
                .year(Integer.valueOf(metadata.get("xmpDM:releaseDate")))
                .build();
    }

}
