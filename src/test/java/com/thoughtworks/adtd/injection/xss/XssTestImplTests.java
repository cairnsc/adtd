package com.thoughtworks.adtd.injection.xss;

import com.thoughtworks.adtd.http.*;
import com.thoughtworks.adtd.http.responseConditions.body.HasContent;
import com.thoughtworks.adtd.http.responseConditions.status.HasStatusCode;
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
        public void shouldRegisterHasStatusCodeConditionIfUnset() throws Exception {
            String testPattern = "<script>";
            createTestRequestAndMockedResponse(testPattern, 200, "test");

            Response result = request.execute(webProxy);

            Collection<ResponseCondition> expectations = request.getExpectations();
            // REVISIT: should assert that it was added only if it was unset
            assertThat(expectations).contains(new HasStatusCode(HttpStatus.OK));
        }

    @Test
    public void shouldRegisterHasContentCondition() throws Exception {
        String testPattern = "<script>";
        createTestRequestAndMockedResponse(testPattern, 200, "test");

        Response result = request.execute(webProxy);

        Collection<ResponseCondition> expectations = request.getExpectations();
        assertThat(expectations).contains(new HasContent(true));
    }

    @Test
    public void shouldExecuteRequestUsingWebProxy() throws Exception {
        String testPattern = "<script>adtd();</script>";
        createTestRequestAndMockedResponse(testPattern, 200, "test");

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
    public void shouldThrowExceptionWhenResponseBodyMatchesPattern() throws Exception {
        String testPattern = "<script>adtd();</script>";
        String content = "<html><body>" + testPattern + "</body></html>";
        createTestRequestAndMockedResponse(testPattern, 200, content);
        when(response.getBody()).thenReturn(content);
        expectedException.expect(AssertionFailureException.class);
        expectedException.expectMessage("HTTP response body contains injected JavaScript");

        request.execute(webProxy);

        test.assertResponse();
    }

    private void createTestRequestAndMockedResponse(String testPattern, int statusCode, String body) throws Exception {
        test = new XssTestImpl(testPattern);
        request = test.prepare();
        webProxy = mock(WebProxy.class);
        response = mock(Response.class);
        when(webProxy.execute(request)).thenReturn(response);
        when(response.getStatus()).thenReturn(statusCode);
        when(response.getBody()).thenReturn(body);
    }

}
