package com.thoughtworks.adtd.html;

/**
 * Represents a field in a Form.
 */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FormField formField = (FormField) o;

        if (!name.equals(formField.name)) return false;
        return value != null ? value.equals(formField.value) : formField.value == null;

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
