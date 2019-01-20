package com.lucian.custom.di.helpers;

public class InterfaceDependentService {
    private Interface1 myImplementation;

    public InterfaceDependentService(Interface1 myImplementation) {
        this.myImplementation = myImplementation;
    }

    public String getFooBar() {
        return "foo" + myImplementation.getBar();
    }
}
