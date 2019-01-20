package com.lucian.custom.di;

import com.lucian.custom.di.exceptions.*;
import com.lucian.custom.di.helpers.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

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

    @Test
    public void createSimpleObjectWithDependency() {
        stringPropertyHandler = Mockito.mock(StringPropertyHandler.class);

        UnityContainer unityContainer = new UnityContainer(stringPropertyHandler);
        unityContainer.addSingleton(SimpleObject.class);
        unityContainer.addSingleton(DependentObject1.class);
        DependentObject1 object = unityContainer.createObject(DependentObject1.class);

        assertNotNull("DependentObject1.class was created", object);
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

    @Test(expected = InterfaceCreationNotAllowedException.class)
    public void testCreatingDirectlyInterfaces() {

        stringPropertyHandler = Mockito.mock(StringPropertyHandler.class);

        UnityContainer unityContainer = new UnityContainer(stringPropertyHandler);
        unityContainer.addSingleton(Interface1.class);

        Interface1 object = unityContainer.createObject(Interface1.class);
    }

    @Test(expected = AbstractCreationNotAllowedException.class)
    public void testCreatingDirectlyAbstractClasses() {

        stringPropertyHandler = Mockito.mock(StringPropertyHandler.class);

        UnityContainer unityContainer = new UnityContainer(stringPropertyHandler);
        unityContainer.addSingleton(AbstractClass.class);

        AbstractClass object = unityContainer.createObject(AbstractClass.class);
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

    @Test
    public void testReceiveStringAsParameter() {
        stringPropertyHandler = Mockito.mock(StringPropertyHandler.class);
        when(stringPropertyHandler.getProperty("test-key")).thenReturn("value1");

        UnityContainer unityContainer = new UnityContainer(stringPropertyHandler);
        unityContainer.addTransient(StringDependentObject.class);

        StringDependentObject object = unityContainer.createObject(StringDependentObject.class);

        assertNotNull("StringDependentObject.class was created", object);
        assertEquals("String dependency was correctly injected by StringPropertyHandler", "value1", object.getTest());
    }

    @Test
    public void testReceiveNullStringAsParameter() {
        stringPropertyHandler = Mockito.mock(StringPropertyHandler.class);

        UnityContainer unityContainer = new UnityContainer(stringPropertyHandler);
        unityContainer.addTransient(StringDependentObject.class);

        StringDependentObject object = unityContainer.createObject(StringDependentObject.class);

        assertNotNull("StringDependentObject.class was created", object);
        assertNull("String dependency does not exists on StringPropertyHandler", object.getTest());
    }

    @Test
    public void testObjectWhenInjectingInterface() {
        stringPropertyHandler = Mockito.mock(StringPropertyHandler.class);

        UnityContainer unityContainer = new UnityContainer(stringPropertyHandler);
        unityContainer.addSingleton(Interface1.class, Service1.class);

        Interface1 object = unityContainer.createObject(Interface1.class);

        assertNotNull("InterfaceDependentService.class was created", object);
        assertEquals("Interface1 object is a instance of Service1 type", Service1.class, object.getClass());
    }

    @Test
    public void testCreateInterfaceDependentService() {
        stringPropertyHandler = Mockito.mock(StringPropertyHandler.class);

        UnityContainer unityContainer = new UnityContainer(stringPropertyHandler);
        unityContainer.addSingleton(InterfaceDependentService.class);
        unityContainer.addSingleton(Interface1.class, Service1.class);

        InterfaceDependentService object = unityContainer.createObject(InterfaceDependentService.class);

        assertNotNull("InterfaceDependentService.class was created", object);
        assertEquals("Foobar message correctly received", "foobar", object.getFooBar());
    }

}
