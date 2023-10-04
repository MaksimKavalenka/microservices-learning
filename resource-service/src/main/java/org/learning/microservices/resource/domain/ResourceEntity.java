package org.learning.microservices.resource.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;

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

    @JsonIgnore
    @JdbcTypeCode(Types.BINARY)
    private byte[] content;

}
