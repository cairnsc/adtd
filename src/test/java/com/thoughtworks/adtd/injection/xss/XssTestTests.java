package com.thoughtworks.adtd.injection.xss;

import com.thoughtworks.adtd.http.*;
import com.thoughtworks.adtd.http.responseConditions.body.HasContent;
import com.thoughtworks.adtd.http.responseConditions.status.HasStatusCode;
import com.thoughtworks.adtd.injection.xss.strategies.TestStrategy;
import com.thoughtworks.adtd.util.AssertionFailureException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class XssTestTests {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private TestStrategy testStrategyMock;
    private WebProxy webProxyMock;
    private XssTest test;
    private Request request;
    private Response responseMock;

    @Test
    public void shouldThrowExceptionIfPrepareInvokedMoreThanOnce() throws Exception {
        createTestAndRequest();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A test request has already been created for this test");

        test.prepare();
    }

    @Test
    public void shouldGetRequestFromTestStrategy() throws Exception {
        createTestAndRequest();

        verify(testStrategyMock).createRequest(test);
    }

    @Test
    public void shouldThrowExceptionWhenNoResponseIsSetInAssertResponse() throws Exception {
        test = new XssTest(mock(TestStrategy.class));
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The test has not yet been executed");

        test.assertResponse();
    }

    @Test
    public void shouldThrowExceptionWhenResponseBodyMatchesPattern() throws Exception {
        String content = "oh noes";
        createTestAndRequestAndMockedResponse(200, content);
        when(responseMock.getBody()).thenReturn(content);
        when(testStrategyMock.matches(content)).thenReturn(true);
        expectedException.expect(AssertionFailureException.class);
        expectedException.expectMessage("HTTP response body contains injected JavaScript");

        request.execute(webProxyMock);

        test.assertResponse();
    }
    @Test
    public void shouldRegisterHasStatusCodeConditionIfUnset() throws Exception {
        createTestAndRequestAndMockedResponse(200, "test");

        Response result = request.execute(webProxyMock);

        Collection<ResponseCondition> expectations = request.getExpectations();
        // REVISIT: should assert that it was added only if it was unset
        assertThat(expectations).contains(new HasStatusCode(HttpStatus.OK));
    }

    @Test
    public void shouldRegisterHasContentCondition() throws Exception {
        createTestAndRequestAndMockedResponse(200, "test");

        Response result = request.execute(webProxyMock);

        Collection<ResponseCondition> expectations = request.getExpectations();
        assertThat(expectations).contains(new HasContent(true));
    }

    @Test
    public void shouldExecuteRequestUsingWebProxy() throws Exception {
        createTestAndRequestAndMockedResponse(200, "test");

        Response result = request.execute(webProxyMock);

        assertThat(responseMock).isEqualTo(result);
        verify(webProxyMock).execute(request);
    }

    private void createTestAndRequest() throws Exception {
        testStrategyMock = mock(TestStrategy.class);
        test = new XssTest(testStrategyMock);
        when(testStrategyMock.createRequest(test)).thenReturn(new RequestImpl(test));
        request = test.prepare();
    }

    private void createTestAndRequestAndMockedResponse(int statusCode, String body) throws Exception {
        createTestAndRequest();
        webProxyMock = mock(WebProxy.class);
        responseMock = mock(Response.class);
        when(webProxyMock.execute(request)).thenReturn(responseMock);
        when(responseMock.getStatus()).thenReturn(statusCode);
        when(responseMock.getBody()).thenReturn(body);
    }
}
