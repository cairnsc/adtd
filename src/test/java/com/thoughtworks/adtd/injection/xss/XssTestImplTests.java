package com.thoughtworks.adtd.injection.xss;

import com.thoughtworks.adtd.http.*;
import com.thoughtworks.adtd.http.responseConditions.ResponseStatusCondition;
import com.thoughtworks.adtd.util.AssertionFailureException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
        test = new XssTestImpl("<script>");
        test.prepare();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A test request has already been created for this test");

        test.prepare();
    }

    @Test
    public void shouldRegisterResponseStatusConditionIfUnset() throws Exception {
        String testPattern = "<script>";
        createTestRequestAndMockedResponse(testPattern, 200);

        Response result = request.execute(webProxy);

        Collection<ResponseCondition> expectations = request.getExpectations();

        // REVISIT: should assert that it was added only if it was unset
        assertThat(expectations).contains(new ResponseStatusCondition(HttpStatus.OK));
    }

    @Test
    public void shouldExecuteRequestUsingWebProxy() throws Exception {
        String testPattern = "<script>adtd();</script>";
        createTestRequestAndMockedResponse(testPattern, 200);

        Response result = request.execute(webProxy);

        assertThat(response).isEqualTo(result);
        verify(webProxy).execute(request);
    }

    @Test
    public void shouldThrowExceptionWhenNoResponseIsSetInAssertResponse() throws Exception {
        test = new XssTestImpl("");
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The test has not yet been executed");

        test.assertResponse();
    }

    @Test
    public void shouldThrowExceptionWhenResponseBodyIsEmpty() throws Exception {
        String testPattern = "<script>";
        createTestRequestAndMockedResponse(testPattern, 200);
        when(response.getBody()).thenReturn("");
        expectedException.expect(AssertionFailureException.class);
        expectedException.expectMessage("HTTP response body is empty");

        request.execute(webProxy);

        test.assertResponse();
    }

    @Test
    public void shouldThrowExceptionWhenResponseBodyIsNull() throws Exception {
        String testPattern = "<script>";
        createTestRequestAndMockedResponse(testPattern, 200);
        when(response.getBody()).thenReturn(null);
        expectedException.expect(AssertionFailureException.class);
        expectedException.expectMessage("HTTP response body is empty");

        request.execute(webProxy);

        test.assertResponse();
    }

    @Test
    public void shouldThrowExceptionWhenResponseBodyMatchesPattern() throws Exception {
        String testPattern = "<script>adtd();</script>";
        createTestRequestAndMockedResponse(testPattern, 200);
        String content = "<html><body>" + testPattern + "</body></html>";
        when(response.getBody()).thenReturn(content);
        expectedException.expect(AssertionFailureException.class);
        expectedException.expectMessage("HTTP response body contains injected JavaScript");

        request.execute(webProxy);

        test.assertResponse();
    }

    private void createTestRequestAndMockedResponse(String testPattern, int statusCode) throws Exception {
        test = new XssTestImpl(testPattern);
        request = test.prepare();
        webProxy = mock(WebProxy.class);
        response = mock(Response.class);
        when(response.getStatus()).thenReturn(statusCode);
        when(webProxy.execute(request)).thenReturn(response);
    }

}
