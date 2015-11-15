package com.thoughtworks.adtd.injection.http;

import com.thoughtworks.adtd.html.Form;
import com.thoughtworks.adtd.html.FormData;
import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.RequestExecutor;
import com.thoughtworks.adtd.http.Response;
import com.thoughtworks.adtd.http.WebProxy;
import com.thoughtworks.adtd.util.AssertionFailureException;

/**
 * Test for HTTP response splitting.
 *
 * Read response splitting at https://www.owasp.org/index.php/HTTP_Response_Splitting
 */
public class HttpResponseSplittingTest implements RequestExecutor {
    public static final String TEST_HEADER = "Bork";
    public static final String TEST_STRING = "test%0d%0a" + TEST_HEADER + ":%20bork";
    private final Form form;
    private final FormData formData;
    private final int fieldIdx;
    private Request request;
    private Response response;

    public HttpResponseSplittingTest(Form form, FormData formData, int fieldIdx) {
        this.form = form;
        this.formData = formData;
        this.fieldIdx = fieldIdx;
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

        request = form.createRequest(this);
        formData.setFormField(fieldIdx, TEST_STRING);
        formData.setImmutable();
        formData.setRequestParams(request);
        return request;
    }

    /**
     * Assert that the response does not fail the test.
     * @throws Exception
     */
    public void assertResponse() throws Exception {
        if (!requestIsComplete()) {
            throw new IllegalStateException("A request must first be prepared and executed for this test");
        }

        String header = response.getHeader(TEST_HEADER);
        if (header != null) {
            throw new AssertionFailureException("HTTP response contains injected header");
        }
    }

    public Response execute(WebProxy proxy) throws Exception {
        return proxy.execute(request);
    }

    public void process(Request request, Response response) throws Exception {
        this.response = response;
    }

    private boolean requestIsComplete() {
        return (request != null && response != null);
    }
}
