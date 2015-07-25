package com.thoughtworks.adtd.csrf.token;

import com.thoughtworks.adtd.csrf.token.strategies.TestStrategy;
import com.thoughtworks.adtd.html.FormData;
import com.thoughtworks.adtd.html.FormDataImpl;
import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.RequestExecutor;
import com.thoughtworks.adtd.http.ResponseValidator;

public class CsrfTokenTestImpl implements CsrfTokenTest {

    private final TestStrategy testStrategy;
    private final String formAction;
    private CsrfTokenRetrieveRequest retrieveRequest;
    private CsrfTokenSubmitRequest submitRequest;
    public RequestExecutor currentRequest;
    private FormData formData;
    private String tokenInputName;
    private final ResponseValidator validator;

    public CsrfTokenTestImpl(TestStrategy testStrategy, String formAction, String tokenInputName, ResponseValidator validator) {
        this.testStrategy = testStrategy;

        this.formAction = formAction;
        this.tokenInputName = tokenInputName;
        this.validator = validator;
    }

    public Request prepareRetrieve() {
        if (retrieveRequest != null) {
            throw new IllegalStateException("A retrieve request has already been prepared for this test");
        }

        retrieveRequest = new CsrfTokenRetrieveRequest(this, formAction, tokenInputName);
        retrieveRequest.prepareRequest();
        currentRequest = retrieveRequest;
        return retrieveRequest.getRequest();
    }

    public void notifyRequestComplete() {
        if (currentRequest == retrieveRequest) {
            formData = new FormDataImpl(retrieveRequest.getForm());
            testStrategy.mutateFormData(formData);
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

        submitRequest = new CsrfTokenSubmitRequest(this, retrieveRequest.getForm(), formData);
        submitRequest.prepareRequest();
        currentRequest = submitRequest;
        return submitRequest.getRequest();
    }

    public void assertResponse() throws Exception {
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
