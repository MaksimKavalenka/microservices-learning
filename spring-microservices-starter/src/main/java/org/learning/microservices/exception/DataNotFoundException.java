package org.learning.microservices.exception;

public class DataNotFoundException extends RuntimeException {

    public DataNotFoundException(Integer id) {
        super(String.format("Data not found by ID: %d", id));
    }

}
