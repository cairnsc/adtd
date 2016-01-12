package com.thoughtworks.adtd.http;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.thoughtworks.adtd.html.FormField;
import com.thoughtworks.adtd.html.RequestParameterProperty;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Request parameters to be transmitted in a request.
 */
public class RequestParameters {
    private final List<RequestParameterImpl> params;
    private boolean isMutable;

    /**
     * Constructor for empty request parameters.
     */
    public RequestParameters() {
        params = newArrayList();
        isMutable = true;
    }

    /**
     * @param formFields Form fields to create {@link RequestParameter}s from.
     */
    public RequestParameters(List<FormField> formFields) {
        params = newArrayList();
        isMutable = true;
        for (FormField formField : formFields) {
            params.add(new RequestParameterImpl(formField.getName(), formField.getValue(), formField.getProperties()));
        }
    }

    /**
     * Add a request parameter. If multiple values are provided, it is treated as a multi-value parameter. The order
     * parameters are added in is preserved.
     * @param name Request parameter name.
     * @param values One or more request parameter values.
     */
    public void param(String name, String... values) {
        checkMutability();
        params.add(new RequestParameterImpl(name, values));
    }

    /**
     * Add a request parameter. If multiple values are provided, it is treated as a multi-value parameter. The order
     * parameters are added in is preserved.
     * @param name Request parameter name.
     * @param values List of zero or more request parameter values.
     * @param properties Request parameter properties.
     */
    public void param(String name, List<String> values, EnumSet<RequestParameterProperty> properties) {
        checkMutability();
        params.add(new RequestParameterImpl(name, values, properties));
    }


    /**
     * Add request parameters from another RequestParameters instance.
     * @param requestParameters RequestParameters to copy request parameters from.
     */
    public void params(RequestParameters requestParameters) {
        for (RequestParameter param : requestParameters.params) {
            params.add(new RequestParameterImpl(param.getName(), param.getValues(), param.getProperties()));
        }
    }

    /**
     * Set a request parameter by name. If a request parameter with the name already exists, it is replaced at its
     * current position in the request parameter list. If more than one request parameter exists with the name, the
     * first is replaced and the remainder are removed. If no request parameters exist with the name, it is added to
     * the end of the request parameter list.
     * @param name Name of request parameter.
     * @param values One or more request parameter values.
     */
    public void setParam(String name, String... values) {
        checkMutability();
        Iterator<RequestParameterImpl> iterator = params.iterator();
        boolean hasParam = false;

        while (iterator.hasNext()) {
            RequestParameterImpl requestParameter = iterator.next();
            if (requestParameter.getName().equals(name)) {
                if (!hasParam) {
                    requestParameter.setValues(values);
                    hasParam = true;
                } else {
                    iterator.remove();
                }
            }
        }

        if (!hasParam) {
            params.add(new RequestParameterImpl(name, values));
        }
    }

    /**
     * Set a request parameter value at an index in the request parameter list. The values for the request parameter at
     * the index are replaced.
     * @param index Index of request parameter.
     * @param values One or more request parameter values.
     */
    public void setParam(int index, String... values) {
        checkMutability();
        params.get(index).setValues(values);
    }

    /**
     * Get request parameters for a parameter name.
     * @param name Request parameter name.
     * @return List of request parameters.
     */
    public List<RequestParameter> getParam(final String name) {
        List<RequestParameter> result = newArrayList();
        for (RequestParameterImpl requestParameter : params) {
            if (requestParameter.getName().equals(name)) {
                result.add(requestParameter);
            }
        }
        return result;
    }

    /**
     * Get a request parameter at an index.
     * @param index Array index of request parameter.
     */
    public RequestParameter getParam(int index) {
        return params.get(index);
    }

    /**
     * Get the indices of request parameters for a parameter name.
     * @param name Request parameter name.
     * @return List of request parameters indexes.
     */
    public List<Integer> paramIndexOf(String name) {
        List<Integer> indices = newArrayList();
        for (int idx = 0; idx < params.size(); idx++) {
            if (params.get(idx).getName().equals(name)) {
                indices.add(idx);
            }
        }
        return indices;
    }

    /**
     * Get a multimap of request parameters. Parameter order is preserved
     * @return Multimap of request parameters.
     */
    public Multimap<String, RequestParameter> getParams() {
        Multimap<String, RequestParameter> result = LinkedListMultimap.create();
        for (RequestParameterImpl requestParameter : params) {
            result.put(requestParameter.getName(), requestParameter);
        }
        return result;
    }

    /**
     * Get a count of the number of request parameters.
     * @return Number of request parameters.
     */
    public int size() {
        return params.size();
    }

    /**
     * Make the request parameters immutable.
     */
    public void setImmutable() {
        isMutable = false;
    }

    /**
     * Determine whether the request parameters are mutable.
     * @return Boolean indicating whether the request parameters are mutable.
     */
    public boolean isMutable() {
        return isMutable;
    }

    private void checkMutability() {
        if (!isMutable) {
            throw new IllegalStateException("RequestParameters is immutable");
        }
    }
}
