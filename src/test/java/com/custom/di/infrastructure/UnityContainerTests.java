package com.custom.di.infrastructure;

import com.custom.di.infrastructure.helpers.Object1;
import com.custom.di.infrastructure.helpers.Object2;
import com.custom.di.infrastructure.helpers.SimpleObject;
import com.custom.di.infrastructure.helpers.ObjectWithNoConstructor;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

public class UnityContainerTests {
    @Mock
    StringPropertyHandler stringPropertyHandler;

    @Test
    public void createSimpleObject() {
        stringPropertyHandler = Mockito.mock(StringPropertyHandler.class);


        UnityContainer unityContainer = new UnityContainer(stringPropertyHandler);
        unityContainer.addSingleton(SimpleObject.class);
        SimpleObject object = unityContainer.createObject(SimpleObject.class);

        assertNotNull("SimpleObject.class was created", object);
    }

    @Test(expected = NotRegisteredClassException.class)
    public void testCreateObjectThatWasNotRegistered() {
        stringPropertyHandler = Mockito.mock(StringPropertyHandler.class);

        UnityContainer unityContainer = new UnityContainer(stringPropertyHandler);
        Object object = unityContainer.createObject(Object.class);
    }

    @Test(expected = CircularDependencyException.class)
    public void testCircularDependency() {

        stringPropertyHandler = Mockito.mock(StringPropertyHandler.class);

        UnityContainer unityContainer = new UnityContainer(stringPropertyHandler);
        unityContainer.addSingleton(Object1.class);
        unityContainer.addSingleton(Object2.class);

        Object1 object = unityContainer.createObject(Object1.class);
    }

    @Test(expected = NoPublicConstructorFoundException.class)
    public void testObjectWithNoConstructor() {

        stringPropertyHandler = Mockito.mock(StringPropertyHandler.class);

        UnityContainer unityContainer = new UnityContainer(stringPropertyHandler);
        unityContainer.addSingleton(ObjectWithNoConstructor.class);

        ObjectWithNoConstructor object = unityContainer.createObject(ObjectWithNoConstructor.class);
    }

    @Test
    public void testCreateSingletonObject() {

        stringPropertyHandler = Mockito.mock(StringPropertyHandler.class);

        UnityContainer unityContainer = new UnityContainer(stringPropertyHandler);
        unityContainer.addSingleton(SimpleObject.class);

        SimpleObject object1 = unityContainer.createObject(SimpleObject.class);
        SimpleObject object2 = unityContainer.createObject(SimpleObject.class);

        assertEquals("Objects are the same", object1, object2);
    }

    @Test
    public void testCreateTransientObject() {

        stringPropertyHandler = Mockito.mock(StringPropertyHandler.class);

        UnityContainer unityContainer = new UnityContainer(stringPropertyHandler);
        unityContainer.addTransient(SimpleObject.class);

        SimpleObject object1 = unityContainer.createObject(SimpleObject.class);
        SimpleObject object2 = unityContainer.createObject(SimpleObject.class);

        assertNotEquals("Objects are not the same", object1, object2);
    }

}
