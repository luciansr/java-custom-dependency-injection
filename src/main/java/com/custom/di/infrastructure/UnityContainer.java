package com.custom.di.infrastructure;

import com.custom.di.services.PropertyHandler;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UnityContainer {

    public enum CreationType {
        TRANSIENT,
        SCOPED,
        SINGLETON
    }

    private HashMap<String, ServiceRegistration> registeredServices;

    private PropertyHandler propertyHandler;

    public UnityContainer(PropertyHandler propertyHandler) {
        registeredServices = new HashMap<>();
        this.propertyHandler = propertyHandler;
    }

    private void register(String key, Type classType, CreationType creationType) {
        if (!registeredServices.containsKey(key)) {
            registeredServices.put(key, new ServiceRegistration(classType, creationType));
        }
    }

    public <T> void AddTransient(Class<T> requestedClass, Class<T> createdClass) {
        register(requestedClass.getTypeName(), createdClass, CreationType.TRANSIENT);
    }

    public <T> void AddTransient(Class<T> clazz) {
        this.AddTransient(clazz, clazz);
    }

    public <T> void AddScoped(Class<T> requestedClass, Class<T> createdClass) {
        register(requestedClass.getTypeName(), createdClass, CreationType.SCOPED);
    }

    public <T> void AddScoped(Class<T> clazz) {
        this.AddScoped(clazz, clazz);
    }

    public <T> void AddSingleton(Class<T> requestedClass, Class<T> createdClass) {
        register(requestedClass.getTypeName(), createdClass, CreationType.SINGLETON);
    }

    public <T> void AddSingleton(Class<T> clazz) {
        this.AddSingleton(clazz, clazz);
    }


    public <T> T createObject(Class<T> clazz) {
        try {
            Constructor<?>[] constructors = Class.forName(clazz.getTypeName()).getConstructors();

            for (Constructor<?> constructor : constructors) {
                if (constructor.toString().startsWith("public ")) {
                    List<Object> constructorParameters = new ArrayList<>();

                    //getting all constructor parameters
                    for (Parameter parameter : constructor.getParameters()) {
                        if (parameter.getType() == String.class) {
                            CustomStringProperty annotation = parameter.getAnnotation(CustomStringProperty.class);

                            String propertyToSearch = annotation.value();
                            String objectParameter = propertyHandler.getProperty(propertyToSearch);
                            constructorParameters.add(objectParameter);

                        } else {
                            Object objectParameter = createObject(parameter.getType());
                            constructorParameters.add(objectParameter);
                        }
                    }

                    //creating object
                    T myObj = (T) constructor.newInstance(constructorParameters.toArray());
                    return myObj;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }
}
