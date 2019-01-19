package com.custom.di.infrastructure;

import java.lang.reflect.Type;

public class ServiceRegistration {
    private Type classType;
    private Factory.CreationType creationType;

    public ServiceRegistration(Type classType, Factory.CreationType creationType) {
        this.classType = classType;
        this.creationType = creationType;
    }

    public Type getClassType() {
        return classType;
    }

    public Factory.CreationType getCreationType() {
        return creationType;
    }
}
