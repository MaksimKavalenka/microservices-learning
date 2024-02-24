package org.learning.microservices.processor.exception;

public class MessageProcessingException extends RuntimeException {

    public MessageProcessingException() {
        super("Exception while processing a message");
    }

}
