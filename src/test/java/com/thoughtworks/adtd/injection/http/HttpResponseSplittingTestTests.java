package com.thoughtworks.adtd.injection.http;

import com.thoughtworks.adtd.html.Form;
import com.thoughtworks.adtd.html.FormData;
import com.thoughtworks.adtd.http.*;
import com.thoughtworks.adtd.testutil.BasicHtml;
import com.thoughtworks.adtd.testutil.TestResponse;
import com.thoughtworks.adtd.util.AssertionFailureException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class HttpResponseSplittingTestTests {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private HttpResponseSplittingTest test;
    private WebProxy webProxyMock;
    private Form formMock;
    private FormData formData;
    private Request request;
    private TestResponse response;

    @Before
    public void setUp() {
        webProxyMock = mock(WebProxy.class);
        formMock = mock(Form.class);
        formData = spy(new FormData());
        formData.addFormField("a", "A");
        formData.addFormField("b", "B");
    }

    @Test
    public void shouldThrowExceptionIfPrepareInvokedMoreThanOnce() throws Exception {
        createTest(0);
        test.prepare();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A request has already been prepared for this test");

        test.prepare();
    }

    @Test
    public void shouldPrepareRequest() throws Exception {
        int fieldIdx = 1;
        createTest(fieldIdx);

        Request request = test.prepare();

        InOrder inOrder = inOrder(formMock, formData);
        inOrder.verify(formMock).createRequest(test);
        inOrder.verify(formData).setFormField(fieldIdx, HttpResponseSplittingTest.TEST_STRING);
        inOrder.verify(formData).setImmutable();
        inOrder.verify(formData).setRequestParams(request);
    }

    @Test
    public void shouldExecuteRequestUsingWebProxy() throws Exception {
        createTest(0);
        createResponse(200, BasicHtml.HTML);

        test.prepare().execute(webProxyMock);

        verify(webProxyMock).execute(request);
    }

    @Test
    public void shouldThrowExceptionInAssertResponseBeforeRequestCreated() throws Exception {
        createTest(0);
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A request must first be prepared and executed for this test");

        test.assertResponse();
    }

    @Test
    public void shouldThrowExceptionInAssertResponseBeforeRequestExecuted() throws Exception {
        createTest(0);
        test.prepare();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A request must first be prepared and executed for this test");

        test.assertResponse();
    }

    @Test
    public void shouldThrowExceptionWhenResponseContainsSplitHeader() throws Exception {
        createTest(0);
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
        createResponse(200, BasicHtml.HTML);
        response.addHeader("Test", "test");
        test.prepare().execute(webProxyMock);

        test.assertResponse();
    }

    private void createTest(int fieldIdx) throws Exception {
        test = new HttpResponseSplittingTest(formMock, formData, fieldIdx);
        request = new RequestImpl(test);
        when(formMock.createRequest(test)).thenReturn(request);
    }

    private Response createResponse(int statusCode, String body) throws Exception {
        webProxyMock = mock(WebProxy.class);
        response = new TestResponse(statusCode, body);
        when(webProxyMock.execute(request)).thenReturn(response);
        return response;
    }
}