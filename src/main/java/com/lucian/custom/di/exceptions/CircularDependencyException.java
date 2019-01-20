package com.lucian.custom.di.exceptions;

public class CircularDependencyException extends RuntimeException {
    public CircularDependencyException(String className) {
        super("Circular dependency found when creating class: " + className + ". Error when trying to inject object.");
    }
}