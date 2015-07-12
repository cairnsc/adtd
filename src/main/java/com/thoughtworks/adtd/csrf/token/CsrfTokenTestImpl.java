package com.thoughtworks.adtd.csrf.token;

import com.thoughtworks.adtd.html.FormData;
import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.RequestExecutor;
import com.thoughtworks.adtd.http.ResponseValidator;

public class CsrfTokenTestImpl implements CsrfTokenTest {

    private CsrfTokenRetrieveRequest retrieveRequest;
    private CsrfTokenSubmitRequest submitRequest;
    public RequestExecutor currentRequest;
    private FormData formData;
    private String tokenInputName;

    public Request prepareRetrieve(String formAction, String tokenInputName) {
        if (retrieveRequest != null) {
            throw new IllegalStateException("A retrieve request has already been prepared for this test");
        }

        retrieveRequest = new CsrfTokenRetrieveRequest(this, formAction, tokenInputName);
        retrieveRequest.prepareRequest();
        currentRequest = retrieveRequest;
        this.tokenInputName = tokenInputName;
        return retrieveRequest.getRequest();
    }

    public void notifyRequestComplete() {
        if (currentRequest == retrieveRequest) {
            formData = new FormData(retrieveRequest.getForm());
        }

        currentRequest = null;
    }

    public FormData getFormData() throws Exception {
        if (!retrieveRequestIsComplete()) {
            throw new IllegalStateException("A retrieve request must first be executed for this test");
        }
        return formData;
    }

    public Request prepareSubmit() throws Exception {
        if (submitRequest != null) {
            throw new IllegalStateException("A submit request has already been prepared for this test");
        }

        if (!retrieveRequestIsComplete()) {
            throw new IllegalStateException("A retrieve request must first be executed for this test");
        }

        submitRequest = new CsrfTokenSubmitRequest(this, retrieveRequest.getForm(), formData, tokenInputName);
        submitRequest.prepareRequest();
        currentRequest = submitRequest;
        return submitRequest.getRequest();
    }

    public void assertResponse(ResponseValidator validator) throws Exception {
        if (!submitRequestIsComplete()) {
            throw new IllegalStateException("A submit request must first be executed for this test");
        }

        validator.validate(submitRequest.getRequest(), submitRequest.getResponse());
    }

    private boolean retrieveRequestIsComplete() {
        return (retrieveRequest != null && currentRequest != retrieveRequest);
    }

    private boolean submitRequestIsComplete() {
        return (submitRequest != null && currentRequest != submitRequest);
    }

}
