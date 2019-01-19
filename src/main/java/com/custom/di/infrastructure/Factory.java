package com.custom.di.infrastructure;

import com.custom.di.services.PropertyHandler;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Factory {

    public enum CreationType {
        TRANSIENT,
        SINGLETON
    }

    private HashMap<String, ServiceRegistration> registeredServices;

    private static Factory instance;
    private PropertyHandler propertyHandler;

    private Factory() {
        registeredServices = new HashMap<>();
        propertyHandler = new PropertyHandler();
    }

    public static Factory getInstance() {
        if (instance == null) {
            instance = new Factory();
        }

        return instance;
    }

    public void register(String key, Type classType, CreationType creationType) {
        if (!registeredServices.containsKey(key)) {
            registeredServices.put(key, new ServiceRegistration(classType, creationType));
        }
    }

    public <T extends Object> T get(String serviceKey) {
        try {
            Constructor<?> constructor = Class.forName(serviceKey).getConstructor();
            Parameter[] parameters = constructor.getParameters();
            CustomStringProperty customAnnotaion = parameters[0].getAnnotation(CustomStringProperty.class);

            System.out.println(customAnnotaion.value());


        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T createObject(Class<T> clazz) {
        try {
            Constructor<?>[] constructors = Class.forName(clazz.getTypeName()).getConstructors();
            //constructors[0].getParameters()[0].getAnnotation(CustomStringProperty.class).value()
            for (Constructor<?> constructor : constructors) {
                if (constructor.toString().startsWith("public ")) {
                    List<Object> constructorParameters = new ArrayList<>();

                    //get all constructor parameters
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

                    //create object

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
