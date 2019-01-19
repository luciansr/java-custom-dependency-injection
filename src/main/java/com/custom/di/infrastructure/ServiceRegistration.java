package com.custom.di.infrastructure;

import java.lang.reflect.Type;

public class ServiceRegistration {
    private Type classType;
    private UnityContainer.CreationType creationType;

    public ServiceRegistration(Type classType, UnityContainer.CreationType creationType) {
        this.classType = classType;
        this.creationType = creationType;
    }

    public Type getClassType() {
        return classType;
    }

    public UnityContainer.CreationType getCreationType() {
        return creationType;
    }
}
