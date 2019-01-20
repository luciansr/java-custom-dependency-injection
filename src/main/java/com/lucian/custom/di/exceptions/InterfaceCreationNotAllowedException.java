package com.lucian.custom.di.exceptions;

public class InterfaceCreationNotAllowedException extends RuntimeException {
    public InterfaceCreationNotAllowedException(String className) {
        super("Interface direct creation detected when trying to create: " + className + ". Error when trying to create object.");
    }
}
