package org.learning.microservices.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ValidationErrorResponse extends ErrorResponse {

    private List<ViolationFieldError> violations;

    @Data
    @Builder
    public static class ViolationFieldError {

        private String field;

        private String message;

    }

}
