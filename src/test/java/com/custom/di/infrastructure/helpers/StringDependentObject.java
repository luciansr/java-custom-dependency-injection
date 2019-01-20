package com.custom.di.infrastructure.helpers;

import com.custom.di.infrastructure.CustomStringProperty;

public class StringDependentObject {
    private String test;
    public StringDependentObject(@CustomStringProperty("test-key") String test) {
        this.test = test;
    }

    public String getTest() {
        return test;
    }
}
