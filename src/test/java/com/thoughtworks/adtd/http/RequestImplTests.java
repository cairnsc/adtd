package com.thoughtworks.adtd.http;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class RequestImplTests {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private RequestExecutor requestExecutorMock;
    private RequestImpl request;

    @Before
    public void setUp() {
        requestExecutorMock = mock(RequestExecutor.class);
        request = new RequestImpl(requestExecutorMock, null);
    }

    @Test
    public void shouldSetMethod() {
        String methodName = "get";

        Request retVal = request.method(methodName);

        assertThat(retVal).isEqualTo(request);
        assertThat(request.getMethod()).isEqualTo(methodName);
    }

    @Test
    public void shouldThrowExceptionInMethodWhenRequestHasAlreadyExecuted() throws Exception {
        executeRequest();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The test has already been executed");

        request.method("GET");
    }

    @Test
    public void shouldSetUri() {
        String uri = "http://adtd/test";

        Request retVal = request.uri(uri);

        assertThat(retVal).isEqualTo(request);
        assertThat(request.getUri()).isEqualTo(uri);
    }

    @Test
    public void shouldThrowExceptionInUriWhenRequestHasAlreadyExecuted() throws Exception {
        executeRequest();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The test has already been executed");

        request.uri("test");
    }

    @Test
    public void shouldAddHeader() {
        String headerName = "Host";
        String headerValue = "1";

        Request retVal = request.header(headerName, headerValue);

        assertThat(retVal).isEqualTo(request);
        List<Header> headers = request.getHeader(headerName);
        assertThat(headers).hasSize(1);
        Header header = headers.get(0);
        assertThat(header.getName()).isEqualTo(headerName);
        assertThat(header.getValue()).isEqualTo(headerValue);
    }

    @Test
    public void shouldAddMultipleHeaders() {
        String headerNames[] = { "Accept", "Host" };
        String headerValues1[] = { "1", "2" };
        String headerValue2 = "3";

        request.header(headerNames[0], headerValues1[0])
                .header(headerNames[0], headerValues1[1])
                .header(headerNames[1], headerValue2);

        assertThat(request.getHeader(headerNames[0])).containsExactly(
                new HeaderImpl(headerNames[0], headerValues1[0]),
                new HeaderImpl(headerNames[0], headerValues1[1])
        );
        assertThat(request.getHeader(headerNames[1])).containsExactly(new HeaderImpl(headerNames[1], headerValue2));
    }

    @Test
    public void shouldThrowExceptionInHeaderWhenRequestHasAlreadyExecuted() throws Exception {
        executeRequest();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The test has already been executed");

        request.header("A", "B");
    }

    @Test
    public void shouldSetHeaderAtIndex() {
        String headerName = "1";
        String headerValue = "2";
        request.header(headerName, headerValue)
                .header(headerName,  "3");

        String replacementValue = "4";
        request.setHeader(1, replacementValue);

        List<Header> headers = request.getHeader(headerName);
        assertThat(headers).hasSize(2);
        assertThat(headers.get(0).getValue()).isEqualTo(headerValue);
        assertThat(headers.get(1).getValue()).isEqualTo(replacementValue);
    }

    @Test
    public void shouldThrowExceptionInSetHeaderWithIndexWhenRequestHasAlreadyExecuted() throws Exception {
        executeRequest();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The test has already been executed");

        request.setHeader(0, "B");
    }

    @Test
    public void shouldMakeRequestParametersImmutable() throws Exception {
        executeRequest();

        RequestParameters requestParams = request.getParams();
        assertThat(requestParams.isMutable()).isFalse();
    }

    @Test
    public void shouldAddParams() {
        String paramName = "A";
        String paramValue = "B";

        Request retVal = request.param(paramName, paramValue).param("C", "d", "e").param("f", "g");

        assertThat(retVal).isEqualTo(request);
        List<RequestParameter> param = request.getParam(paramName);
        assertThat(param).hasSize(1);
        assertThat(param.get(0)).isEqualTo(new RequestParameterImpl(paramName, paramValue));
        assertThat(request.paramIndexOf(paramName)).containsExactly(0);
        assertThat(request.getParam(0)).isSameAs(param.get(0));
    }


    @Test
    public void shouldExecuteAgainstRequestExecutor() throws Exception {
        WebProxy webProxyMock = mock(WebProxy.class);
        Response responseMock = mock(Response.class);
        when(requestExecutorMock.execute(webProxyMock)).thenReturn(responseMock);

        Response result = request.execute(webProxyMock);

        verify(requestExecutorMock).execute(webProxyMock);
        assertThat(result).isEqualTo(responseMock);
    }

    @Test
    public void shouldThrowExceptionWhenRequestHasAlreadyExecuted() throws Exception {
        WebProxy webProxyMock = mock(WebProxy.class);
        when(requestExecutorMock.execute(webProxyMock)).thenReturn(mock(Response.class));
        request.execute(webProxyMock);
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The test has already been executed");

        request.execute(webProxyMock);
    }

    @Test
    public void shouldThrowExceptionIfExecutorExecuteReturnsNull() throws Exception {
        WebProxy webProxyMock = mock(WebProxy.class);
        expectedException.expect(InvalidResponseException.class);
        expectedException.expectMessage("RequestExecutor execute returned null");

        request.execute(webProxyMock);
    }

    @Test
    public void shouldGetResponse() throws Exception {
        WebProxy webProxyMock = mock(WebProxy.class);
        Response responseMock = mock(Response.class);
        when(requestExecutorMock.execute(webProxyMock)).thenReturn(responseMock);

        request.execute(webProxyMock);

        assertThat(request.getResponse()).isEqualTo(responseMock);
    }

    @Test
    public void shouldNotAddResponseConditionOfSameTypeTwiceInExpectIfUnset() {
        ResponseCondition condition1 = new TestResponseCondition("1");
        ResponseCondition condition2 = mock(ResponseCondition.class);
        ResponseCondition condition3 = new TestResponseCondition("3");

        request.expectIfUnset(condition1)
                .expectIfUnset(condition2)
                .expectIfUnset(condition3);

        List<ResponseCondition> expectations = request.getExpectations();
        assertThat(expectations).hasSize(2);
        assertThat(expectations).contains(condition1);
        assertThat(expectations).contains(condition2);
    }

    @Test
    public void shouldVerifyConditionsInOrder() throws Exception {
        WebProxy webProxyMock = mock(WebProxy.class);
        Response responseMock = mock(Response.class);
        when(requestExecutorMock.execute(webProxyMock)).thenReturn(responseMock);
        ResponseCondition condition1 = mock(ResponseCondition.class);
        ResponseCondition condition2 = mock(ResponseCondition.class);
        request.expect(condition1).expect(condition2);

        request.execute(webProxyMock);

        InOrder inOrder = inOrder(condition1, condition2);
        inOrder.verify(condition1).match(request, responseMock);
        inOrder.verify(condition2).match(request, responseMock);
    }

    @Test
    public void shouldThrowExceptionInExpectWhenRequestHasAlreadyExecuted() throws Exception {
        executeRequest();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The test has already been executed");

        request.expect(mock(ResponseCondition.class));
    }

    @Test
    public void shouldThrowExceptionInExpectIfUnsetWhenRequestHasAlreadyExecuted() throws Exception {
        executeRequest();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The test has already been executed");

        request.expectIfUnset(mock(ResponseCondition.class));
    }

    @Test
    public void shouldThrowExceptionInProcessWithWhenRequestHasAlreadyExecuted() throws Exception {
        executeRequest();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The test has already been executed");

        request.processWith(mock(ResponseProcessor.class));
    }

    @Test
    public void shouldPrepareResponseProcessors() throws Exception {
        WebProxy webProxyMock = mock(WebProxy.class);
        Response responseMock = mock(Response.class);
        when(requestExecutorMock.execute(webProxyMock)).thenReturn(responseMock);
        ResponseProcessor responseProcessorMock1 = mock(ResponseProcessor.class);
        ResponseProcessor responseProcessorMock2 = mock(ResponseProcessor.class);
        request.processWith(responseProcessorMock1).processWith(responseProcessorMock2);

        request.execute(webProxyMock);

        InOrder inOrder = inOrder(responseProcessorMock1, responseProcessorMock2, requestExecutorMock);
        inOrder.verify(responseProcessorMock1).prepare(request);
        inOrder.verify(responseProcessorMock2).prepare(request);
        inOrder.verify(requestExecutorMock).execute(webProxyMock);
    }

    @Test
    public void shouldVerifyResponseConditions() throws Exception {
        WebProxy webProxyMock = mock(WebProxy.class);
        Response responseMock = mock(Response.class);
        when(requestExecutorMock.execute(webProxyMock)).thenReturn(responseMock);
        ResponseCondition responseConditionMock1 = mock(ResponseCondition.class);
        ResponseCondition responseConditionMock2 = mock(ResponseCondition.class);
        request.expect(responseConditionMock1).expect(responseConditionMock2);
        ResponseProcessor responseProcessorMock = mock(ResponseProcessor.class);
        request.processWith(responseProcessorMock);

        request.execute(webProxyMock);

        InOrder inOrder = inOrder(requestExecutorMock, responseConditionMock1, responseConditionMock2);
        inOrder.verify(requestExecutorMock).execute(webProxyMock);
        verify(responseConditionMock1).match(request, responseMock);
        verify(responseConditionMock2).match(request, responseMock);
    }

    @Test
    public void shouldProcessResponseWithResponseProcessors() throws Exception {
        WebProxy webProxyMock = mock(WebProxy.class);
        Response responseMock = mock(Response.class);
        when(requestExecutorMock.execute(webProxyMock)).thenReturn(responseMock);
        ResponseCondition responseConditionMock = mock(ResponseCondition.class);
        request.expect(responseConditionMock);
        ResponseProcessor responseProcessorMock1 = mock(ResponseProcessor.class);
        ResponseProcessor responseProcessorMock2 = mock(ResponseProcessor.class);
        request.processWith(responseProcessorMock1).processWith(responseProcessorMock2);

        request.execute(webProxyMock);

        InOrder inOrder = inOrder(responseConditionMock, responseProcessorMock1, responseProcessorMock2);
        inOrder.verify(responseConditionMock).match(request, responseMock);
        inOrder.verify(responseProcessorMock1).process(request, responseMock);
        inOrder.verify(responseProcessorMock2).process(request, responseMock);
    }

    @Test
    public void shouldProcessResponseWithRequestExecutor() throws Exception {
        WebProxy webProxyMock = mock(WebProxy.class);
        Response responseMock = mock(Response.class);
        when(requestExecutorMock.execute(webProxyMock)).thenReturn(responseMock);
        ResponseProcessor responseProcessorMock = mock(ResponseProcessor.class);
        request.processWith(responseProcessorMock);

        request.execute(webProxyMock);

        InOrder inOrder = inOrder(responseProcessorMock, requestExecutorMock);
        inOrder.verify(responseProcessorMock).process(request, responseMock);
        inOrder.verify(requestExecutorMock).process(request, responseMock);
    }

    private void executeRequest() throws Exception {
        WebProxy webProxyMock = mock(WebProxy.class);
        Response responseMock = mock(Response.class);
        when(requestExecutorMock.execute(webProxyMock)).thenReturn(responseMock);
        request.execute(webProxyMock);
    }

    private class TestResponseCondition implements ResponseCondition {
        private final String name;

        public TestResponseCondition(String name) {
            this.name = name;
        }

        public void match(Request request, Response response) throws Exception {
        }
    }
}
