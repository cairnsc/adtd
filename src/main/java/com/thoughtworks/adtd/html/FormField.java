package com.thoughtworks.adtd.html;

public class FormField {
    private String name;
    private String value;

    public FormField(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
