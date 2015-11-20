package com.thoughtworks.adtd.injection.xss;

import com.thoughtworks.adtd.http.*;
import com.thoughtworks.adtd.injection.xss.strategies.TestStrategy;
import com.thoughtworks.adtd.util.AssertionFailureException;

import static com.thoughtworks.adtd.http.ResponseConditionFactory.body;
import static com.thoughtworks.adtd.http.ResponseConditionFactory.status;

/**
 * Simple test for Cross-site Scripting (XSS). Performs basic pattern matching against a response.
 *
 * Read about XSS at https://www.owasp.org/index.php/Cross-site_Scripting_(XSS)
 * XSS filter evasion cheat sheet: https://www.owasp.org/index.php/XSS_Filter_Evasion_Cheat_Sheet
 */
public class XssTest implements RequestExecutor {
    private final TestStrategy testStrategy;
    private Request request;

    public XssTest(TestStrategy testStrategy) {
        this.testStrategy = testStrategy;
    }

    public TestStrategy getTestStrategy() {
        return testStrategy;
    }

    /**
     * Prepare a request to perform the test.
     * @return Request.
     * @throws Exception
     */
    public Request prepare() throws Exception {
        if (requestIsPrepared()) {
            throw new IllegalStateException("A test request has already been created for this test");
        }

        request = testStrategy.createRequest(this);

        return request;
    }

    /**
     * Assert that the response does not fail the test.
     * @throws Exception
     */
    public void assertResponse() throws Exception {
        if (!requestIsComplete()) {
            throw new IllegalStateException("The test has not yet been executed");
        }

        if (testStrategy.matches(request.getResponse().getBody())) {
            throw new AssertionFailureException("HTTP response body contains injected JavaScript");
        }
    }

    public Response execute(WebProxy proxy) throws Exception {
        request.expectIfUnset(status().is(HttpStatus.OK));
        request.expect(body().isNotNullOrEmpty());
        return proxy.execute(request);
    }

    public void process(Request request, Response response) throws Exception {
    }

    private boolean requestIsPrepared() {
        return (request != null);
    }

    private boolean requestIsComplete() {
        return (request != null && request.getResponse() != null);
    }
}
