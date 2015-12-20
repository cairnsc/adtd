package com.thoughtworks.adtd.http;

import com.thoughtworks.adtd.html.Form;
import com.thoughtworks.adtd.html.FormField;

import java.util.List;

/**
 * Holds parameters and data to be used when creating a HTTP request.
 */
public class RequestInfo {
    private String method;
    private String uri;
    private RequestParameters requestParameters;
    private boolean isMutable = true;

    /**
     * Create RequestInfo.
     * @param method Request method.
     * @param uri Request URI.
     */
    public RequestInfo(String method, String uri) {
        this.method = method;
        this.uri = uri;
        requestParameters = new RequestParameters();
    }

    /**
     * Create RequestInfo.
     * @param method Request method.
     * @param uri Request URI.
     * @param formFields Form fields to be submitted in the request.
     */
    public RequestInfo(String method, String uri, List<FormField> formFields) {
        this.method = method;
        this.uri = uri;
        requestParameters = new RequestParameters(formFields);
    }

    /**
     * Create RequestInfo from the contents of Form.
     * @param form Form.
     */
    public RequestInfo(Form form) throws Exception {
        method = form.getMethod();
        uri = form.getAction();
        requestParameters = new RequestParameters(form.getFormFields());
    }

    /**
     * Set this request info as immutable.
     */
    public void setImmutable() {
        isMutable = false;
        requestParameters.setImmutable();
    }

    /**
     * Determine whether this request info is mutable.
     * @return Boolean indicating whether this request info is mutable.
     */
    public boolean isMutable() {
        return isMutable;
    }

    /**
     * Get the request method.
     * @return Request method, or null if none is set.
     */
    public String getMethod() {
        return method;
    }

    /**
     * Set the request method.
     * @param method Request method.
     */
    public void setMethod(String method) {
        checkMutability();
        this.method = method;
    }

    /**
     * Get the request URI.
     * @return Request URI, or null if none is set.
     */
    public String getUri() {
        return uri;
    }

    /**
     * Set the request URI.
     * @param uri Request URI, or null if none is set.
     */
    public void setUri(String uri) {
        checkMutability();
        this.uri = uri;
    }

    /**
     * Get request parameters to be submitted in the request.
     * @return
     */
    public RequestParameters getRequestParameters() {
        return requestParameters;
    }

    /**
     * Build a request.
     * @param requestExecutor Request executor.
     * @return Created request.
     * @throws Exception
     */
    public Request createRequest(RequestExecutor requestExecutor) throws Exception {
        Request request = new RequestImpl(requestExecutor)
                .method(method)
                .uri(uri);
        request.getParams().params(requestParameters);
        return request;
    }

    private void checkMutability() {
        if (!isMutable) {
            throw new IllegalStateException("RequestInfo is immutable");
        }
    }
}