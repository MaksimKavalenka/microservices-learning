package org.learning.resource.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(Integer id) {
        super(String.format("Resource not found by ID: %d", id));
    }

}
