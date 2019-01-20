package com.lucian.custom.di.infrastructure.exceptions;

public class NotRegisteredClassException extends RuntimeException {
    public NotRegisteredClassException(String className) {
        super("Not registered class: " + className + ". Error when trying to create object.");
    }
}
