package com.lucian.custom.di.exceptions;

public class AbstractCreationNotAllowedException extends RuntimeException {
    public AbstractCreationNotAllowedException(String className) {
        super("Abstract class direct creation detected when trying to create: " + className + ". Error when trying to create object.");
    }
}
