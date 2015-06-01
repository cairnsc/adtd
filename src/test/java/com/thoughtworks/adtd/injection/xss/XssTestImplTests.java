package com.thoughtworks.adtd.injection.xss;

import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.Response;
import com.thoughtworks.adtd.http.WebProxy;
import com.thoughtworks.adtd.util.AssertionFailureException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class XssTestImplTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private WebProxy webProxy;
    private XssTestImpl test;
    private Request request;
    private Response response;

    @Test
    public void shouldReturnTestString() {
        String testPattern = "<script>";
        test = new XssTestImpl(testPattern);

        assertThat(test.getTestPattern()).isEqualTo(testPattern);
    }

    @Test
    public void shouldMatchPattern() {
        String testPattern = "<script>adtd();</script>";
        test = new XssTestImpl(testPattern);
        String content = "<html><body>" + testPattern + "</body></html>";

        boolean matches = test.matches(content);

        assertThat(matches).isTrue();
    }

    @Test
    public void shouldMatchPatternCaseInsensitive() {
        String testPattern = "<script>adtd();</script>";
        XssTestImpl test = new XssTestImpl(testPattern);
        String content = "<html><body>" + testPattern.toUpperCase() + "</body></html>";

        boolean matches = test.matches(content);

        assertThat(matches).isTrue();
    }

    @Test
    public void shouldThrowExceptionIfPrepareInvokedMoreThanOnce() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A test request has already been created for this test");
        test = new XssTestImpl("");
        test.prepare();

        test.prepare();
    }

    @Test
    public void shouldExecuteRequestUsingWebProxy() throws Exception {
        String testPattern = "<script>adtd();</script>";
        createTestRequestAndMockedResponse(testPattern);

        Response result = request.execute(webProxy);

        assertThat(response).isEqualTo(result);
        verify(webProxy).execute(request);
    }

    @Test
    public void shouldThrowExceptionWhenNoResponseIsSetInAssertResponse() throws Exception {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The test has not yet been executed");
        test = new XssTestImpl("");
        webProxy = mock(WebProxy.class);

        test.prepare().execute(webProxy);

        test.assertResponse();
    }

    @Test
    public void shouldThrowExceptionWhenResponseStatusIsNotExpected() throws Exception {
        int expectedStatusCode = 123;
        int actualStatusCode = 321;
        expectedException.expect(AssertionFailureException.class);
        expectedException.expectMessage("HTTP response status code: expected \"" + expectedStatusCode + "\", actual \"" + actualStatusCode + "\"");
        String testPattern = "";
        createTestRequestAndMockedResponse(testPattern);
        request.expectStatusCode(expectedStatusCode);
        when(response.getStatus()).thenReturn(actualStatusCode);

        request.execute(webProxy);

        test.assertResponse();
    }

    @Test
    public void shouldThrowExceptionWhenResponseBodyIsEmpty() throws Exception {
        expectedException.expect(AssertionFailureException.class);
        expectedException.expectMessage("HTTP response body is empty");
        String testPattern = "";
        createTestRequestAndMockedResponse(testPattern);
        when(response.getStatus()).thenReturn(200);
        when(response.getBody()).thenReturn("");

        request.execute(webProxy);

        test.assertResponse();
    }

    @Test
    public void shouldThrowExceptionWhenResponseBodyIsNull() throws Exception {
        expectedException.expect(AssertionFailureException.class);
        expectedException.expectMessage("HTTP response body is empty");
        String testPattern = "";
        createTestRequestAndMockedResponse(testPattern);
        when(response.getStatus()).thenReturn(200);
        when(response.getBody()).thenReturn(null);

        request.execute(webProxy);

        test.assertResponse();
    }

    @Test
    public void shouldThrowExceptionWhenResponseBodyMatchesPattern() throws Exception {
        expectedException.expect(AssertionFailureException.class);
        expectedException.expectMessage("HTTP response body contains injected JavaScript");
        String testPattern = "<script>adtd();</script>";
        createTestRequestAndMockedResponse(testPattern);
        String content = "<html><body>" + testPattern + "</body></html>";
        when(response.getStatus()).thenReturn(200);
        when(response.getBody()).thenReturn(content);

        request.execute(webProxy);

        test.assertResponse();
    }

    private void createTestRequestAndMockedResponse(String testPattern) throws Exception {
        test = new XssTestImpl(testPattern);
        request = test.prepare();
        webProxy = mock(WebProxy.class);
        response = mock(Response.class);
        when(webProxy.execute(request)).thenReturn(response);
    }

}
