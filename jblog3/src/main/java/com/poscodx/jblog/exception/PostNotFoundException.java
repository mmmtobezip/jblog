package com.poscodx.jblog.exception;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException() {
        super("Blog Not Found Exception Occurs");
    }

    public PostNotFoundException(String message) {
        super(message);
    }
}