package com.thoughtworks.adtd.csrf;

import com.thoughtworks.adtd.http.Request;
import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;

import java.util.List;

public class CsrfTokenTestImpl implements CsrfTokenTest {

    private CsrfTokenRetrieveRequest retrieveRequest;
    private CsrfTokenSubmitRequest submitRequest;
    public CsrfTokenTestRequest currentRequest;
    private List<Connection.KeyVal> formData;

    public Request prepareRetrieve(String formAction, String tokenInputName) {
        if (retrieveRequest != null) {
            throw new IllegalStateException("A retrieve request has already been created for this test");
        }

        retrieveRequest = new CsrfTokenRetrieveRequest(this, formAction, tokenInputName);
        currentRequest = retrieveRequest;
        return retrieveRequest.getRequest();
    }

    public void notifyRequestComplete() {
        currentRequest = null;
    }

    public void setFormData(String name, String value) {
        if (submitRequest != null) {
            throw new IllegalStateException("Unable to set form data after the submit request is created");
        }

        HttpConnection.KeyVal keyVal = HttpConnection.KeyVal.create(name, value);
        formData.add(keyVal);
    }

    public Request prepareSubmit() {
        if (submitRequest != null) {
            throw new IllegalStateException("A submit request has already been created for this test");
        }

        if (retrieveRequest == null || currentRequest != null) {
            throw new IllegalStateException("A retrieve request must first be executed for this test");
        }

        submitRequest = new CsrfTokenSubmitRequest(this, retrieveRequest.getForm(), formData);
        return submitRequest.getRequest();
    }

    public void assertResponse() throws Exception {
    }

}
