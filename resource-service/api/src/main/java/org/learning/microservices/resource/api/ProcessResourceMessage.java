package org.learning.microservices.resource.api;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProcessResourceMessage implements Serializable {

    private Integer id;

    private String s3Key;

}
