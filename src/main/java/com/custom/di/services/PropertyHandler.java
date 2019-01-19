package com.custom.di.services;

import com.custom.di.infrastructure.StringPropertyHandler;

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
