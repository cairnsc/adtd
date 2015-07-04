package com.thoughtworks.adtd.csrf;

import com.thoughtworks.adtd.html.Form;
import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.RequestExecutor;
import com.thoughtworks.adtd.http.Response;
import com.thoughtworks.adtd.http.WebProxy;
import org.jsoup.Connection;

import java.util.List;

public class CsrfTokenSubmitRequest implements CsrfTokenTestRequest, RequestExecutor {

    private final CsrfTokenTestImpl testOrchestrator;
    private Request request;

    public CsrfTokenSubmitRequest(CsrfTokenTestImpl testOrchestrator, Form form, List<Connection.KeyVal> formData) {
        this.testOrchestrator = testOrchestrator;
    }

    public Request getRequest() {
        return request;
    }

    public Response execute(WebProxy proxy) throws Exception {
        return null;
    }

    public void process(Request request, Response response) throws Exception {
    }


}
