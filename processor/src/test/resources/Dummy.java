package com.nonpool.application;

import com.nonpool.annotation.Getter;

@Getter
public class Dummy {
    private int id;

    private String name;

    public Dummy(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
