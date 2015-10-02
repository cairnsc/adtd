package com.thoughtworks.adtd.csrf.token;

import com.thoughtworks.adtd.csrf.token.strategies.TestStrategy;
import com.thoughtworks.adtd.html.Form;
import com.thoughtworks.adtd.html.FormData;
import com.thoughtworks.adtd.http.*;

import static com.thoughtworks.adtd.http.ResponseConditionFactory.status;

public class CsrfTokenTestImpl implements CsrfTokenTest, RequestExecutor {
    private final TestStrategy testStrategy;
    private final String tokenInputName;
    private final Form form;
    private final FormData formData;
    private final ResponseValidator validator;
    private Request request;
    private Response response;

    public CsrfTokenTestImpl(TestStrategy testStrategy, String tokenInputName, Form form, FormData formData,
                             ResponseValidator validator) {
        this.testStrategy = testStrategy;
        this.tokenInputName = tokenInputName;
        this.form = form;
        this.formData = formData;
        this.validator = validator;
    }

    public Request prepare() throws Exception {
        if (request != null) {
            throw new IllegalStateException("A request has already been prepared for this test");
        }

        request = form.createRequest(this);
        testStrategy.mutateFormData(formData);
        formData.setImmutable();
        formData.setRequestParams(request);
        return request;
    }

    public Response execute(WebProxy proxy) throws Exception {
        request.expectIfUnset(status().is(HttpStatus.OK));
        return proxy.execute(request);
    }

    public void process(Request request, Response response) throws Exception {
        this.response = response;
    }

    public void assertResponse() throws Exception {
        if (!requestIsComplete()) {
            throw new IllegalStateException("A request must first be prepared and executed for this test");
        }

        validator.validate(request, response);
    }

    private boolean requestIsComplete() {
        return (request != null && response != null);
    }
}
