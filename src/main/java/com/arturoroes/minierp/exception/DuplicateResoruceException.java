package com.arturoroes.minierp.exception;

public class DuplicateResoruceException extends RuntimeException {

    public DuplicateResoruceException(String message) {
        super(message);
    }

    public DuplicateResoruceException(String resource, String field, Object value) {
        super(String.format("%s already exists with %s:'%s'", resource, field, value));
    }
}
