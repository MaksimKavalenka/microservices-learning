package org.learning.microservices.processor.exception;

public class MessageProcessingException extends Exception {

    public MessageProcessingException() {
        super("Exception while processing a message");
    }

}
