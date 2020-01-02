package com.nonpool.application;

public class Application {

    public static void main(String[] args) {
        Dummy dummy = new Dummy(1,"Lily");
        System.out.println(dummy.getId());

        DummyForField dummyForField = new DummyForField(18, "Nana");
        System.out.println(dummyForField.getAge());
        System.out.println(dummyForField.getName());

        System.out.println(DummyFactory.dummy);
    }
}
