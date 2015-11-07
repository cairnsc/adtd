package com.thoughtworks.adtd.csrf.token;

import com.thoughtworks.adtd.csrf.token.strategies.TestStrategy;
import com.thoughtworks.adtd.html.Form;
import com.thoughtworks.adtd.html.FormData;
import com.thoughtworks.adtd.http.*;

import static com.thoughtworks.adtd.http.ResponseConditionFactory.status;

/**
 * Test to validate CSRF protection using the synchronizer token pattern.
 *
 * Read about the synchronizer token pattern at https://www.owasp.org/index.php/Cross-Site_Request_Forgery_%28CSRF%29_Prevention_Cheat_Sheet#General_Recommendation:_Synchronizer_Token_Pattern
 */
public class CsrfTokenTest implements RequestExecutor {
    private final TestStrategy testStrategy;
    private final Form form;
    private final FormData formData;
    private final ResponseValidator validator;
    private Request request;
    private Response response;

    public CsrfTokenTest(TestStrategy testStrategy, Form form, FormData formData, ResponseValidator validator) {
        this.testStrategy = testStrategy;
        this.form = form;
        this.formData = formData;
        this.validator = validator;
    }

    /**
     * Prepare a request to submit the form with the CSRF token from the retrieve request included.
     * @return Request.
     * @throws Exception
     */
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
