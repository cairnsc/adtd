package com.thoughtworks.adtd.csrf;

import com.thoughtworks.adtd.http.Request;

public class CsrfTokenTestImpl implements CsrfTokenTest {

    private CsrfTokenRetrieveRequest retrieveRequest;

    public Request prepareRetrieve(String formAction, String tokenInputName) {
        if (retrieveRequest != null) {
            throw new IllegalStateException("A retrieval request has already been created for this test");
        }

        retrieveRequest = new CsrfTokenRetrieveRequest(formAction, tokenInputName);
        return retrieveRequest.getRequest();
    }

    public void setInput(String name, String value) {
    }

    public Request prepareSubmit() {
        return null;
    }

    public void assertResponse() throws Exception {
    }

}
