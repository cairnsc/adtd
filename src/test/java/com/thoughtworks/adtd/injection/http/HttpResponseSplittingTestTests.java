package com.thoughtworks.adtd.injection.http;

import com.thoughtworks.adtd.http.*;
import com.thoughtworks.adtd.testutil.BasicHtml;
import com.thoughtworks.adtd.testutil.TestResponse;
import com.thoughtworks.adtd.util.AssertionFailureException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class HttpResponseSplittingTestTests {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private HttpResponseSplittingTest test;
    private WebProxy webProxyMock;
    private RequestInfo requestInfoMock;
    private Request request;
    private TestResponse response;
    private RequestParameters requestParametersMock;

    @Before
    public void setUp() {
        webProxyMock = mock(WebProxy.class);
        requestInfoMock = mock(RequestInfo.class);
    }

    @Test
    public void shouldThrowExceptionIfPrepareInvokedMoreThanOnce() throws Exception {
        createTest(0);
        request.param("a", "b");
        test.prepare();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A request has already been prepared for this test");

        test.prepare();
    }

    @Test
    public void shouldPrepareRequest() throws Exception {
        int paramIndex = 1;
        String replacedParam = "c";
        createTest(paramIndex);
        request.param("a", "b").param(replacedParam, "d");

        test.prepare();

        verify(requestInfoMock).createRequest(test, null);
        List<RequestParameter> param = request.getParam(replacedParam);
        assertThat(param).hasSize(1);
        assertThat(param.get(0).getValues()).containsExactly(HttpResponseSplittingTest.TEST_STRING);
    }

    @Test
    public void shouldExecuteRequestUsingWebProxy() throws Exception {
        createTest(0);
        request.param("a", "b");
        createResponse(200, BasicHtml.HTML);

        test.prepare().execute(webProxyMock);

        verify(webProxyMock).execute(request);
    }

    @Test
    public void shouldThrowExceptionInAssertResponseBeforeRequestCreated() throws Exception {
        createTest(0);
        request.param("a", "b");
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A request must first be prepared and executed for this test");

        test.assertResponse();
    }

    @Test
    public void shouldThrowExceptionInAssertResponseBeforeRequestExecuted() throws Exception {
        createTest(0);
        request.param("a", "b");
        test.prepare();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A request must first be prepared and executed for this test");

        test.assertResponse();
    }

    @Test
    public void shouldThrowExceptionWhenResponseContainsSplitHeader() throws Exception {
        createTest(0);
        request.param("a", "b");
        createResponse(200, BasicHtml.HTML);
        response.addHeader(HttpResponseSplittingTest.TEST_HEADER, "test");
        test.prepare().execute(webProxyMock);
        expectedException.expect(AssertionFailureException.class);
        expectedException.expectMessage("HTTP response contains injected header");

        test.assertResponse();
    }

    @Test
    public void shouldPassWhenResponseLacksSplitHeader() throws Exception {
        createTest(0);
        request.param("a", "b");
        createResponse(200, BasicHtml.HTML);
        response.addHeader("Test", "test");
        test.prepare().execute(webProxyMock);

        test.assertResponse();
    }

    private void createTest(int paramIndex) throws Exception {
        test = new HttpResponseSplittingTest(requestInfoMock, paramIndex);
        request = new RequestImpl(test, null);
        when(requestInfoMock.createRequest(test, null)).thenReturn(request);
    }

    private Response createResponse(int statusCode, String body) throws Exception {
        webProxyMock = mock(WebProxy.class);
        response = new TestResponse(statusCode, body);
        when(webProxyMock.execute(request)).thenReturn(response);
        return response;
    }
}