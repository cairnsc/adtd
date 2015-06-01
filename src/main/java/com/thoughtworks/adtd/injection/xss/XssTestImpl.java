package com.thoughtworks.adtd.injection.xss;

import com.thoughtworks.adtd.http.*;
import com.thoughtworks.adtd.util.AssertionFailureException;

import java.util.regex.Pattern;

public class XssTestImpl implements XssTest, RequestExecutor {

    private String testPattern;
    private Request request;

    public XssTestImpl(String testPattern) {
        this.testPattern = testPattern;
    }

    public String getTestPattern() {
        return testPattern;
    }

    public boolean matches(String content) {
        return Pattern.compile(Pattern.quote(testPattern), Pattern.CASE_INSENSITIVE).matcher(content).find();
    }

    public Request prepare() {
        if (request != null) {
            throw new IllegalStateException("A test request has already been created for this test");
        }

        request = new RequestImpl(this);

        return request;
    }

    public Response execute(WebProxy proxy) throws Exception {
        return proxy.execute(request);
    }

    public void assertResponse() throws Exception {
        if (request == null || request.getResponse() == null) {
            throw new IllegalStateException("The test has not yet been executed");
        }

        Response response = request.getResponse();
        if (response.getStatus() != request.getExpectedStatusCode()) {
            throw new AssertionFailureException("HTTP response status code", request.getExpectedStatusCode(), response.getStatus());
        }

        String body = response.getBody();
        if (body == null || body.isEmpty()) {
            throw new AssertionFailureException("HTTP response body is empty");
        }

        if (matches(body)) {
            throw new AssertionFailureException("HTTP response body contains injected JavaScript");
        }
    }

}
