package com.lucian.custom.di;

import java.lang.reflect.Type;

public class ServiceRegistration {
    private Class<?> classType;
    private UnityContainer.CreationType creationType;

    public ServiceRegistration(Class<?> classType, UnityContainer.CreationType creationType) {
        this.classType = classType;
        this.creationType = creationType;
    }

    public Class<?> getClassType() {
        return classType;
    }

    public UnityContainer.CreationType getCreationType() {
        return creationType;
    }
}
