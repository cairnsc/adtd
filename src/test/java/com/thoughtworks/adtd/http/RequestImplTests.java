package com.thoughtworks.adtd.http;

import com.google.common.collect.Multimap;
import com.thoughtworks.adtd.util.AssertionFailureException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class RequestImplTests {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private RequestExecutor requestExecutor;
    private RequestImpl request;

    @Before
    public void setUp() {
        requestExecutor = mock(RequestExecutor.class);
        request = new RequestImpl(requestExecutor);
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
    public void shouldSetHeader() {
        String headerName = "Host";
        String headerValue = "1";

        Request retVal = request.header(headerName, headerValue);

        assertThat(retVal).isEqualTo(request);
        Multimap<String, String> headers = request.getHeaders();
        Collection<String> headers1 = headers.get(headerName);
        assertThat(headers1).containsExactly(headerValue);
    }

    @Test
    public void shouldSetMultipleHeaders() {
        String headerNames[] = { "Accept", "Host" };
        String headerValues1[] = { "1", "2" };
        String headerValue2 = "3";

        request.header(headerNames[0], headerValues1[0])
                .header(headerNames[0], headerValues1[1])
                .header(headerNames[1], headerValue2);

        Multimap<String, String> headers = request.getHeaders();
        Collection<String> headers1 = headers.get(headerNames[0]);
        assertThat(headers1).containsExactly(headerValues1);
        Collection<String> headers2 = headers.get(headerNames[1]);
        assertThat(headers2).containsExactly(headerValue2);
    }

    @Test
    public void shouldThrowExceptionInHeaderWhenRequestHasAlreadyExecuted() throws Exception {
        executeRequest();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The test has already been executed");

        request.header("A", "B");
    }

    @Test
    public void shouldSetParam() {
        String paramName = "A";
        String paramValue = "B";

        Request retVal = request.param(paramName, paramValue);

        assertThat(retVal).isEqualTo(request);
        Multimap<String, String> params = request.getParams();
        Collection<String> params1 = params.get(paramName);
        assertThat(params1).containsExactly(paramValue);
    }

    @Test
    public void shouldSetMultipleParams() {
        String paramNames[] = { "A", "B" };
        String paramValues1[] = { "1", "2" };
        String paramValue2 = "3";

        request.param(paramNames[0], paramValues1[0])
                .param(paramNames[0], paramValues1[1])
                .param(paramNames[1], paramValue2);

        Multimap<String, String> params = request.getParams();
        Collection<String> params1 = params.get(paramNames[0]);
        assertThat(params1).containsExactly(paramValues1);
        Collection<String> params2 = params.get(paramNames[1]);
        assertThat(params2).containsExactly(paramValue2);
    }

    @Test
    public void shouldThrowExceptionInParamWhenRequestHasAlreadyExecuted() throws Exception {
        executeRequest();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The test has already been executed");

        request.param("A", "B");
    }

    @Test
    public void shouldExecuteAgainstRequestExecutor() throws Exception {
        WebProxy webProxy = mock(WebProxy.class);
        Response response = mock(Response.class);
        when(requestExecutor.execute(webProxy)).thenReturn(response);

        Response result = request.execute(webProxy);

        verify(requestExecutor).execute(webProxy);
        assertThat(result).isEqualTo(response);
    }

    @Test
    public void shouldThrowExceptionWhenRequestHasAlreadyExecuted() throws Exception {
        WebProxy webProxy = mock(WebProxy.class);
        when(requestExecutor.execute(webProxy)).thenReturn(mock(Response.class));
        request.execute(webProxy);
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The test has already been executed");

        request.execute(webProxy);
    }

    @Test
    public void shouldThrowExceptionIfExecutorExecuteReturnsNull() throws Exception {
        WebProxy webProxy = mock(WebProxy.class);
        expectedException.expect(InvalidResponseException.class);
        expectedException.expectMessage("RequestExecutor execute returned null");

        request.execute(webProxy);
    }

    @Test
    public void shouldGetResponse() throws Exception {
        WebProxy webProxy = mock(WebProxy.class);
        Response response = mock(Response.class);
        when(requestExecutor.execute(webProxy)).thenReturn(response);

        request.execute(webProxy);

        assertThat(request.getResponse()).isEqualTo(response);
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
        assertThat(expectations.size()).isEqualTo(2);
        assertThat(expectations).contains(condition1);
        assertThat(expectations).contains(condition2);
    }

    @Test
    public void shouldVerifyConditionsInOrder() throws Exception {
        WebProxy webProxy = mock(WebProxy.class);
        Response response = mock(Response.class);
        when(requestExecutor.execute(webProxy)).thenReturn(response);
        ResponseCondition condition1 = mock(ResponseCondition.class);
        ResponseCondition condition2 = mock(ResponseCondition.class);
        request.expect(condition1).expect(condition2);

        request.execute(webProxy);

        InOrder inOrder = inOrder(condition1, condition2);
        inOrder.verify(condition1).match(request, response);
        inOrder.verify(condition2).match(request, response);
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
    public void shouldInvokeResponseProcessorsInOrder() throws Exception {
        WebProxy webProxy = mock(WebProxy.class);
        Response response = mock(Response.class);
        when(requestExecutor.execute(webProxy)).thenReturn(response);
        ResponseProcessor processor1 = mock(ResponseProcessor.class);
        ResponseProcessor processor2 = mock(ResponseProcessor.class);
        request.processWith(processor1)
                .processWith(processor2);

        request.execute(webProxy);

        InOrder inOrder = inOrder(processor1, processor2);
        inOrder.verify(processor1).process(request, response);
        inOrder.verify(processor2).process(request, response);
    }

    @Test
    public void shouldVerifyConditionsBeforeInvokingResponseProcessors() throws Exception {
        WebProxy webProxy = mock(WebProxy.class);
        Response response = mock(Response.class);
        when(requestExecutor.execute(webProxy)).thenReturn(response);
        String conditionName = "RequestImplTest";
        TestResponseCondition condition = new TestResponseCondition(conditionName, true);
        request.expect(condition);
        ResponseProcessor processor = mock(ResponseProcessor.class);
        request.processWith(processor);

        try {
            request.execute(webProxy);
        } catch (AssertionFailureException ex) {
            assertThat(ex.getMessage()).isEqualTo(conditionName);
        }

        verify(processor, never()).process(request, response);
    }

    private void executeRequest() throws Exception {
        WebProxy webProxy = mock(WebProxy.class);
        Response response = mock(Response.class);
        when(requestExecutor.execute(webProxy)).thenReturn(response);
        request.execute(webProxy);
    }

    private class TestResponseCondition implements ResponseCondition {
        private final String name;
        private final boolean shouldThrow;

        public TestResponseCondition(String name) {
            this(name, false);
        }

        public TestResponseCondition(String name, boolean shouldThrow) {
            this.name = name;
            this.shouldThrow = shouldThrow;
        }

        public void match(Request request, Response response) throws Exception {
            if (shouldThrow) {
                throw new AssertionFailureException(name);
            }
        }
    }
}
