package com.thoughtworks.adtd.http;

public class HeaderImpl implements Header {
    private final String name;
    private String value;

    public HeaderImpl(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public boolean nameEquals(String name) {
        return this.name.equalsIgnoreCase(name);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HeaderImpl header = (HeaderImpl) o;

        if (!nameEquals(header.getName())) return false;
        return value != null ? value.equals(header.value) : header.value == null;

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
