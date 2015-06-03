package com.thoughtworks.adtd.injection.xss;

import com.thoughtworks.adtd.http.*;
import com.thoughtworks.adtd.util.AssertionFailureException;

import java.util.regex.Pattern;

import static com.thoughtworks.adtd.http.ResponseConditionFactory.body;
import static com.thoughtworks.adtd.http.ResponseConditionFactory.status;

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
        request.expectIfUnset(status().is(HttpStatus.OK));
        request.expect(body().isNotNullOrEmpty());
        return proxy.execute(request);
    }

    public void process(Request request, Response response) throws Exception {
    }

    public void assertResponse() throws Exception {
        if (request == null || request.getResponse() == null) {
            throw new IllegalStateException("The test has not yet been executed");
        }

        String body = request.getResponse().getBody();

        if (matches(body)) {
            throw new AssertionFailureException("HTTP response body contains injected JavaScript");
        }
    }

}
