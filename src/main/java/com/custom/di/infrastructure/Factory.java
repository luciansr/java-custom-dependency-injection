package com.custom.di.infrastructure;

import com.custom.di.services.MyService;
import com.custom.di.services.OtherService;
import com.custom.di.services.PropertyHandler;

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
        unityContainer.AddSingleton(MyService.class);
        unityContainer.AddSingleton(OtherService.class);
    }

    public <T> T get(Class<T> clazz) {
        return unityContainer.createObject(clazz);
    }
}
