package org.learning.microservices.resource.entity;

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
@Table(name = "resources")
public class ResourceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resource_generator")
    @SequenceGenerator(name = "resource_generator", sequenceName = "resource_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "s3_key")
    private String s3Key;

}
