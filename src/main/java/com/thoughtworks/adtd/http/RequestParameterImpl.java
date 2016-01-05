package com.thoughtworks.adtd.http;

import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class RequestParameterImpl implements RequestParameter {
    private final String name;
    private List<String> values;

    public RequestParameterImpl(String name, String value) {
        this.name = name;
        values = newArrayList(value);
    }

    public RequestParameterImpl(String name, String... values) {
        this.name = name;
        this.values = newArrayList(values);
    }

    public RequestParameterImpl(String name, List<String> values) {
        this.name = name;
        this.values = newArrayList(values);
    }

    public String getName() {
        return name;
    }

    public List<String> getValues() {
        return Collections.unmodifiableList(values);
    }

    public void setValues(String... values) {
        this.values = newArrayList(values);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestParameterImpl that = (RequestParameterImpl) o;

        if (!name.equals(that.name)) return false;
        return values.equals(that.values);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + values.hashCode();
        return result;
    }
}
