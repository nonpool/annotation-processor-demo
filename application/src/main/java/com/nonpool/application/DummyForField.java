package com.nonpool.application;

import com.nonpool.annotation.Getter;

public class DummyForField {

    @Getter
    private long age;
    @Getter
    private String name;

    public DummyForField(long age, String name) {
        this.age = age;
        this.name = name;
    }
}
