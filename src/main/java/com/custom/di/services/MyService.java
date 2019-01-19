package com.custom.di.services;

import com.custom.di.infrastructure.CustomStringProperty;

public class MyService {
    private final OtherService service;
    private final String key;

    public MyService(
            @CustomStringProperty("key1")String key,
            OtherService service) {
        this.key = key;
        this.service = service;
    }

    public String getKey() {
        return key;
    }
}
