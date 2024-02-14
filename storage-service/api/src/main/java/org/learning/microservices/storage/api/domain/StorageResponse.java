package org.learning.microservices.storage.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageResponse {

    private Integer id;

    private String storageType;

    private String bucketName;

    private String path;

}
