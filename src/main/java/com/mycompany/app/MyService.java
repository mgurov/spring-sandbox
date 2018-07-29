package com.mycompany.app;

public class MyService {
    private final String fixedValue;

    public MyService(String fixedValue) {
        this.fixedValue = fixedValue;
    }

    public String getValue() {
        return fixedValue;
    }
}
