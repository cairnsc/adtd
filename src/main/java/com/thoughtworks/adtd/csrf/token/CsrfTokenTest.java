package com.thoughtworks.adtd.csrf.token;

import com.thoughtworks.adtd.csrf.token.strategies.TestStrategy;
import com.thoughtworks.adtd.http.*;

import static com.thoughtworks.adtd.http.ResponseConditionFactory.status;

/**
 * Test to verify CSRF protection where the synchronizer token pattern is used.
 *
 * <p>To run the test:<br>
 *  1. Prepare a Request with {@link #prepare()}.<br>
 *  2. Modify the Request as needed.<br>
 *  3. Execute the test with {@link #execute(WebProxy)}.<br>
 *  4. Verify the response with {@link #assertResponse()}.
 */
public class CsrfTokenTest implements RequestExecutor {
    private final TestStrategy testStrategy;
    private final RequestInfo requestInfo;
    private final ResponseValidator validator;
    private Request request;
    private Response response;

    public CsrfTokenTest(TestStrategy testStrategy, RequestInfo requestInfo, ResponseValidator validator) {
        this.testStrategy = testStrategy;
        this.requestInfo = requestInfo;
        this.validator = validator;
    }

    /**
     * Prepare a request to perform the test.
     * @return Request.
     * @throws Exception
     */
    public Request prepare() throws Exception {
        if (request != null) {
            throw new IllegalStateException("A request has already been prepared for this test");
        }

        request = requestInfo.createRequest(this);
        testStrategy.mutateRequest(request);
        return request;
    }

    public Response execute(WebProxy proxy) throws Exception {
        request.expectIfUnset(status().is(HttpStatus.OK));
        return proxy.execute(request);
    }

    public void process(Request request, Response response) throws Exception {
        this.response = response;
    }

    /**
     * Assert that the CSRF request succeeded.
     * @throws Exception
     */
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
