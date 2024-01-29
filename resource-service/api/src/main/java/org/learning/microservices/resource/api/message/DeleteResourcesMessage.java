package org.learning.microservices.resource.api.message;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeleteResourcesMessage implements Serializable {

    private String ids;

}
