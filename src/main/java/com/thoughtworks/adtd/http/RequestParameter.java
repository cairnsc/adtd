package com.thoughtworks.adtd.http;

import com.thoughtworks.adtd.html.RequestParameterProperty;

import java.util.EnumSet;
import java.util.List;

/**
 * Represents a request parameter or multi-parameter. Contains 0 or more values.
 */
public interface RequestParameter {
    /**
     * Get the case-sensitive name of the request parameter.
     * @return Request parameter name.
     */
    String getName();

    /**
     * Get values of the request parameter. A parameter can contain 0 or more values.
     * @return Request parameter values.
     */
    List<String> getValues();

    /**
     * Get properties for the request parameter.
     * @return Properties.
     */
    EnumSet<RequestParameterProperty> getProperties();
}
