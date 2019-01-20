# java-custom-dependency-injection

![Pull requests accepted!](https://img.shields.io/badge/PRs-Accepted-brightgreen.svg)

## When to use this library

This library was created with the intention of making possible that old Java projects be able to use Dependency Injection and Inversion of Control. 
Most recent frameworks already have it. So if you use Spring Boot, for instance, this is not for you.


## How to use this library

Suppose that you have 2 objects and that one depends on the other:

#### OtherService.java

```java
package com.my.company.services;

public class OtherService {
    public OtherService() {

    }
}
```

#### MyService.java

```java
package com.my.company.services;

import com.lucian.custom.di.CustomValue;

public class MyService {
    private final OtherService service;
    private final String key;

    public MyService(
            @CustomValue("key1") String key,
            OtherService service) {
        this.key = key;
        this.service = service;
    }

    public String getKey() {
        return key;
    }
}
```

You just need to create a Factory that will be responsible for creating all your objects:

#### Factory.java

```java
package com.my.company.infrastructure;

import com.my.company.services;
import com.lucian.custom.di.UnityContainer;

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
```

Now you just have to call the created Factory, asking for the object that you want to create:

```java
MyService myService = Factory.getInstance().get(MyService.class);
```

#### PropertyHandler.java
The PropertyHandler class is a class that implements ```StringPropertyHandler```. 
It will be used to get all ```string dependencies``` of your classes.

```java
package com.my.company.infrastructure;

import com.lucian.custom.di.StringPropertyHandler;

public class PropertyHandler implements StringPropertyHandler {
    public String getProperty(String key) {
        switch (key) {
            case "key1":
                return "value1";
            default:
                return "default";
        }

    }
}
```

So with the above created ```PropertyHandler``` class, when ```MyService``` asks for a ```@CustomValue("key1") String key``` 
in its ```constructor```, it will be delivered as a ```String``` with value ```"value1"```.



## Types of creation

Have in mind that you can register your dependencies in two ways:

#### Singleton Type
```java
unityContainer.addSingleton(MyService.class);
```
When you register a dependency as a singleton, every time that you ask for it, you will receive the same instance.

#### Transient Type
```java
unityContainer.addTransient(MyService.class);
```

When you register a dependency as a transient object, every time that you ask for it, a new instance will be created.

## Inversion of Control

You can inject ```Classes``` that implement ```Interfaces```!

Suppose that we have these two services:

```java
package com.my.company.services;

public class Service1 {
    public Service1(Interface1 myImplementation) {
        myImplementation.doSomething();
    }
}
```

```java
package com.my.company.services;

public class Service2 implements Interface1 {
    @Override
    public void doSomething() {
        //do something
    }
}
```
And an interface:

```java
package com.my.company.services;

public interface Interface1 {
    void doSomething();
}
```

You can inject ```Service2``` whenever a class depends on ```Interface1```:

```java
unityContainer.addSingleton(Interface1.class, Service2.class);
```

Note that ```Service2``` has to implement (or to extend, if Interface2 was a class) ```Interface2```. 

## Runtime injection of Objects

Maybe this is the finest feature of this library (or not). It can inject objects at runtime. 
With that you can, for example, have two implementations of a feature and choose which to use depending on an external config:

```java
if(MY_EXTERNAL_CONFIG_SAYS_TO) {
    unityContainer.addSingleton(ClassX.class, ImplementationOneOfClassX.class);
} else {
    unityContainer.addSingleton(ClassX.class, ImplementationTwoOfClassX.class);
}
```

## Warnings 

1. You cannot have circular dependencies.
2. All your registered objects have one public constructor (if you have more than one, it cannot guarantee which one will be used).
3. If you don't have public constructors, your object will not be created.
4. If you did not explicitly register your object. It will not be created.
5. You cannot create an Abstract class.
6. You cannot create an Interface.