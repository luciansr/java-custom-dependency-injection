package com.custom.di.services;

public class PropertyHandler {
    public String getProperty(String key) {
        switch (key) {
            case "key1":
                return "value1";
            default:
                return "default";
        }

    }
}
