package com.lucian.custom.di.exceptions;

public class NoPublicConstructorFoundException extends RuntimeException {
    public NoPublicConstructorFoundException(String className) {
        super("No public constructor found when creating class: " + className + ". Error when trying to create object.");
    }
}