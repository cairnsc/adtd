package com.thoughtworks.adtd.csrf;

import com.thoughtworks.adtd.html.FormData;
import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.RequestExecutor;

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
        currentRequest = retrieveRequest;
        return submitRequest.getRequest();
    }

    public void assertResponse() throws Exception {
    }

    private boolean retrieveRequestIsComplete() {
        return (retrieveRequest != null && currentRequest != retrieveRequest);
    }

}
