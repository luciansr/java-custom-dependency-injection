package com.custom.di.infrastructure;

public class NotRegisteredClassException extends RuntimeException {
    public NotRegisteredClassException(String className) {
        super("Not registered class: " + className + ". Error when trying to inject object.");
    }
}
