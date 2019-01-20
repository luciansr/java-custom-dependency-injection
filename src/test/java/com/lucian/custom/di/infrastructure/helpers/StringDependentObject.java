package com.lucian.custom.di.infrastructure.helpers;

import com.lucian.custom.di.infrastructure.CustomValue;

public class StringDependentObject {
    private String test;
    public StringDependentObject(@CustomValue("test-key") String test) {
        this.test = test;
    }

    public String getTest() {
        return test;
    }
}
