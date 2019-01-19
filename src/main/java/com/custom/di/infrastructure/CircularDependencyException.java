package com.custom.di.infrastructure;

public class CircularDependencyException extends RuntimeException {
    public CircularDependencyException(String className) {
        super("Circular dependency found when creating class: " + className + ". Error when trying to inject object.");
    }
}