package com.thoughtworks.adtd.csrf;

import com.thoughtworks.adtd.html.Form;
import com.thoughtworks.adtd.html.FormData;
import com.thoughtworks.adtd.http.*;

import static com.thoughtworks.adtd.http.ResponseConditionFactory.status;

public class CsrfTokenSubmitRequest implements RequestExecutor {

    private final CsrfTokenTestImpl testOrchestrator;
    private final Form form;
    private final FormData formData;
    private final String tokenInputName;
    private Request request;

    public CsrfTokenSubmitRequest(CsrfTokenTestImpl testOrchestrator, Form form, FormData formData, String tokenInputName) {
        this.testOrchestrator = testOrchestrator;
        this.form = form;
        this.formData = formData;
        this.tokenInputName = tokenInputName;
    }

    public void prepareRequest() throws Exception {
        request = form.createRequest(this);
    }

    public Request getRequest() {
        return request;
    }

    public Response execute(WebProxy proxy) throws Exception {
        request.expectIfUnset(status().is(HttpStatus.OK));
        return proxy.execute(request);
    }

    public void process(Request request, Response response) throws Exception {
        testOrchestrator.notifyRequestComplete();
    }

}
