package com.custom.di.services;

import com.custom.di.infrastructure.UnityContainer;

public class Factory {
    private static Factory instance;
    private UnityContainer unityContainer;

    private Factory() {
        unityContainer = new UnityContainer(new PropertyHandler());
        register();
    }

    public static Factory getInstance() {
        if (instance == null) {
            instance = new Factory();
        }

        return instance;
    }

    public void register() {
        unityContainer.addSingleton(MyService.class);
        unityContainer.addSingleton(OtherService.class);
    }

    public <T> T get(Class<T> clazz) {
        return unityContainer.createObject(clazz);
    }
}
