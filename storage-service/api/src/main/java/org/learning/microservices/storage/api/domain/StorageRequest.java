package org.learning.microservices.storage.api.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageRequest {

    @NotBlank
    private String storageType;

    @NotBlank
    private String bucketName;

    @NotBlank
    private String path;

}
