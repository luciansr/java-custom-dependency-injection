package com.lucian.custom.di.infrastructure;

import com.lucian.custom.di.infrastructure.exceptions.CircularDependencyException;
import com.lucian.custom.di.infrastructure.exceptions.NoPublicConstructorFoundException;
import com.lucian.custom.di.infrastructure.exceptions.NotRegisteredClassException;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class UnityContainer {

    public enum CreationType {
        TRANSIENT,
        SINGLETON
    }

    private HashMap<String, Object> createdSingletonObjects;
    private HashMap<String, ServiceRegistration> registeredServices;

    private StringPropertyHandler propertyHandler;

    public UnityContainer(StringPropertyHandler propertyHandler) {
        registeredServices = new HashMap<>();
        createdSingletonObjects = new HashMap<>();

        this.propertyHandler = propertyHandler;
    }

    private void register(String key, Type classType, CreationType creationType) {
        if (!registeredServices.containsKey(key)) {
            registeredServices.put(key, new ServiceRegistration(classType, creationType));
        }
    }

    public <T, TExtension extends T> void addTransient(Class<T> requestedClass, Class<TExtension> createdClass) {
        register(requestedClass.getTypeName(), createdClass, CreationType.TRANSIENT);
    }

    public <T> void addTransient(Class<T> clazz) {
        this.addTransient(clazz, clazz);
    }

    public <T, TExtension extends T> void addSingleton(Class<T> requestedClass, Class<TExtension> createdClass) {
        register(requestedClass.getTypeName(), createdClass, CreationType.SINGLETON);
    }

    public <T> void addSingleton(Class<T> clazz) {
        this.addSingleton(clazz, clazz);
    }

    private <T> boolean isRegistered(Class<T> clazz) {
        return registeredServices.containsKey(clazz.getTypeName());
    }

    private <T> T getPreviouslyCreatedObject (Type clazz, CreationType creationType) {
        switch (creationType) {
            case TRANSIENT:
                return getTransientObject(clazz.getTypeName());
            case SINGLETON:
                return getSingletonObject(clazz.getTypeName());
            default: return null;
        }
    }

    private <T> T getTransientObject (String classTypeName) {
        return null;
    }

    private <T> T getSingletonObject (String classTypeName) {
        return (T) createdSingletonObjects.get(classTypeName);
    }

    private void registerCreatedOject(String classTypeName, CreationType creationType, Object object) {
        switch (creationType) {
            case TRANSIENT:
                return;
            case SINGLETON:
                createdSingletonObjects.put(classTypeName, object);
                return;
            default: return;
        }
    }

    private <T> T createObject(Class<T> clazz, HashSet<String> dependecyList) {
        if (!isRegistered(clazz)) {
            throw new NotRegisteredClassException(clazz.getTypeName());
        }

        ServiceRegistration serviceRegistration = registeredServices.get(clazz.getTypeName());
        T existentObject = getPreviouslyCreatedObject(serviceRegistration.getClassType(), serviceRegistration.getCreationType());
        if(existentObject != null) return existentObject;

        String requestedClassTypeName = clazz.getTypeName();
        String deliveredClassTypeName = serviceRegistration.getClassType().getTypeName();

        if(dependecyList == null) {
            dependecyList = new HashSet<>();
        }

        if (dependecyList.contains(requestedClassTypeName)) {
            throw new CircularDependencyException(requestedClassTypeName);
        }

        dependecyList.add(requestedClassTypeName);

        try {
            Constructor<?>[] constructors = Class.forName(deliveredClassTypeName).getConstructors();

            for (Constructor<?> constructor : constructors) {
                if (constructor.toString().startsWith("public ")) {
                    List<Object> constructorParameters = new ArrayList<>();

                    //getting all constructor parameters
                    for (Parameter parameter : constructor.getParameters()) {
                        if (parameter.getType() == String.class) {
                            CustomValue annotation = parameter.getAnnotation(CustomValue.class);

                            String propertyToSearch = annotation.value();
                            String objectParameter = propertyHandler.getProperty(propertyToSearch);
                            constructorParameters.add(objectParameter);

                        } else {
                            Object objectParameter = createObject(parameter.getType(), dependecyList);
                            constructorParameters.add(objectParameter);
                        }
                    }

                    //creating object
                    T myObj = (T) constructor.newInstance(constructorParameters.toArray());

                    registerCreatedOject(requestedClassTypeName, serviceRegistration.getCreationType(), myObj);

                    return myObj;
                }
            }

            throw new NoPublicConstructorFoundException(deliveredClassTypeName);
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

    public <T> T createObject(Class<T> clazz) {
        return createObject(clazz, null);
    }
}
