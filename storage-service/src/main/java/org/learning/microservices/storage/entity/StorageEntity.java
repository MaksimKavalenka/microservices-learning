package org.learning.microservices.storage.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "storages")
public class StorageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "storage_generator")
    @SequenceGenerator(name = "storage_generator", sequenceName = "storage_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "storage_type")
    private String storageType;

    @Column(name = "bucket_name")
    private String bucketName;

    private String path;

}
