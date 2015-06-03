package com.thoughtworks.adtd.csrf;

import com.thoughtworks.adtd.http.*;
import com.thoughtworks.adtd.util.AssertionFailureException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import static com.thoughtworks.adtd.http.ResponseConditionFactory.status;

public class CsrfTokenTestImpl implements CsrfTokenTest {

    private Request retrieveRequest;
    private String formAction;
    private String tokenInputName;
    private Request submitRequest;

    public Request prepareRetrieve(String formAction, String tokenInputName) {
        if (retrieveRequest != null) {
            throw new IllegalStateException("A retrieval request has already been created for this test");
        }

        this.formAction = formAction;
        this.tokenInputName = tokenInputName;
        retrieveRequest = new RequestImpl(new RequestExecutor() {
            public Response execute(WebProxy proxy) throws Exception {
                return executeRetrieve(proxy);
            }

            public void process(Request request, Response response) throws Exception {
                processRetrieve(request, response);
            }
        })
                .method("GET");

        return retrieveRequest;
    }

    private Response executeRetrieve(WebProxy proxy) throws Exception {
        retrieveRequest.expectIfUnset(status().is(HttpStatus.OK));
        return proxy.execute(retrieveRequest);
    }

    private void processRetrieve(Request request, Response response) throws Exception {
    }

    public void setInput(String name, String value) {
    }

    public Request prepareSubmit() {
        return null;
    }

    public void assertResponse() throws Exception {
    }

}
