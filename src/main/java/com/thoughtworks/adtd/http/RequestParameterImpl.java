package com.thoughtworks.adtd.http;

import com.thoughtworks.adtd.html.RequestParameterProperty;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class RequestParameterImpl implements RequestParameter {
    private final String name;
    private List<String> values;
    private EnumSet<RequestParameterProperty> properties;

    public RequestParameterImpl(String name, String value) {
        this.name = name;
        values = newArrayList(value);
        this.properties = EnumSet.noneOf(RequestParameterProperty.class);
    }

    public RequestParameterImpl(String name, String value, EnumSet<RequestParameterProperty> properties) {
        this.name = name;
        values = newArrayList(value);
        this.properties = EnumSet.copyOf(properties);
    }

    public RequestParameterImpl(String name, String... values) {
        this.name = name;
        this.values = newArrayList(values);
        this.properties = EnumSet.noneOf(RequestParameterProperty.class);
    }

    public RequestParameterImpl(String name, List<String> values, EnumSet<RequestParameterProperty> properties) {
        this.name = name;
        this.values = newArrayList(values);
        this.properties = EnumSet.copyOf(properties);
    }

    public String getName() {
        return name;
    }

    public List<String> getValues() {
        return Collections.unmodifiableList(values);
    }

    public EnumSet<RequestParameterProperty> getProperties() {
        return properties;
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
