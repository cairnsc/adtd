package com.thoughtworks.adtd.csrf.token;

import com.thoughtworks.adtd.csrf.token.strategies.TestStrategy;
import com.thoughtworks.adtd.http.*;
import com.thoughtworks.adtd.http.responseConditions.status.HasStatusCode;
import com.thoughtworks.adtd.testutil.BasicHtml;
import com.thoughtworks.adtd.testutil.TestResponse;
import com.thoughtworks.adtd.util.AssertionFailureException;
import com.thoughtworks.adtd.util.failureMessages.Failure;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class CsrfTokenTestTests {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private WebProxy webProxyMock;
    private TestStrategy testStrategyMock;
    private ResponseValidator responseValidatorMock;
    private CsrfTokenTest test;
    private Request request;
    private Response response;
    private RequestInfo requestInfoMock;

    @Test
    public void shouldGetIsPositiveTestFromTestStrategy() throws Exception {
        createTest();
        boolean expected = true;
        when(testStrategyMock.isPositiveTest()).thenReturn(expected);

        boolean actual = test.isPositiveTest();

        verify(testStrategyMock).isPositiveTest();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldThrowExceptionIfPrepareInvokedMoreThanOnce() throws Exception {
        createTest();
        test.prepare();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A request has already been prepared for this test");

        test.prepare();
    }

    @Test
    public void shouldPrepareRequest() throws Exception {
        createTest();
        String strategyName = "test strategy";
        when(testStrategyMock.getStrategyName()).thenReturn(strategyName);

        test.prepare();

        InOrder inOrder = inOrder(requestInfoMock, testStrategyMock);
        inOrder.verify(requestInfoMock).createRequest(test, "CSRF token test: " + strategyName);
        inOrder.verify(testStrategyMock).mutateRequest(request);
    }

    @Test
    public void shouldExecuteRequestUsingWebProxy() throws Exception {
        createTest();

        Response result = prepareAndExecuteRequest(HttpStatus.OK.getStatusCode(), BasicHtml.HTML);

        assertThat(result).isEqualTo(response);
        verify(webProxyMock).execute(request);
    }

    @Test
    public void shouldThrowExceptionInAssertResponseBeforeRequestCreated() throws Exception {
        createTest();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A request must first be prepared and executed for this test");

        test.assertResponse();
    }

    @Test
    public void shouldThrowExceptionInAssertResponseBeforeRequestExecuted() throws Exception {
        createTest();
        test.prepare();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A request must first be prepared and executed for this test");

        test.assertResponse();
    }

    @Test
    public void shouldThrowExceptionIfResponseFailsValidation() throws Exception {
        createTest();
        prepareAndExecuteRequest(403, BasicHtml.HTML);
        when(responseValidatorMock.validate(test, request, response)).thenReturn(false);
        expectedException.expect(AssertionFailureException.class);
        expectedException.expectMessage(Failure.failure("Response validation", request.getContext()));

        test.assertResponse();
    }

    @Test
    public void shouldSucceed() throws Exception {
        createTest();
        prepareAndExecuteRequest(200, BasicHtml.HTML);
        when(responseValidatorMock.validate(test, request, response)).thenReturn(true);

        test.assertResponse();
    }

    private void createTest() throws Exception {
        testStrategyMock = mock(TestStrategy.class);
        requestInfoMock = mock(RequestInfo.class);
        responseValidatorMock = mock(ResponseValidator.class);
        test = new CsrfTokenTest(testStrategyMock, requestInfoMock, responseValidatorMock);
        request = createRequest();
        when(requestInfoMock.createRequest(eq(test), any(String.class))).thenReturn(request);
    }

    private Request createRequest() throws Exception {
        Request request = new RequestImpl(test, "test request context");
        request.method("POST");
        return request;
    }

    private Response prepareAndExecuteRequest(int statusCode, String body) throws Exception {
        Request preparedRequest = test.prepare();
        assertThat(preparedRequest).isSameAs(request);
        response = createResponse(statusCode, body);
        return request.execute(webProxyMock);
    }

    private Response createResponse(int statusCode, String body) throws Exception {
        webProxyMock = mock(WebProxy.class);
        Response response = new TestResponse(statusCode, body);
        when(webProxyMock.execute(request)).thenReturn(response);
        return response;
    }
}
