package com.thoughtworks.adtd.http;

import com.thoughtworks.adtd.util.MultiValueMap;
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
        WebProxy webProxy = mock(WebProxy.class);
        Response response = mock(Response.class);
        when(requestExecutor.execute(webProxy)).thenReturn(response);
        request.execute(webProxy);
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
        WebProxy webProxy = mock(WebProxy.class);
        Response response = mock(Response.class);
        when(requestExecutor.execute(webProxy)).thenReturn(response);

        request.execute(webProxy);
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
        MultiValueMap<String, String> headers = request.getHeaders();
        List<String> headers1 = headers.get(headerName);
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

        MultiValueMap<String, String> headers = request.getHeaders();
        List<String> headers1 = headers.get(headerNames[0]);
        assertThat(headers1).containsExactly(headerValues1);
        List<String> headers2 = headers.get(headerNames[1]);
        assertThat(headers2).containsExactly(headerValue2);
    }

    @Test
    public void shouldThrowExceptionInHeaderWhenRequestHasAlreadyExecuted() throws Exception {
        WebProxy webProxy = mock(WebProxy.class);
        Response response = mock(Response.class);
        when(requestExecutor.execute(webProxy)).thenReturn(response);
        request.execute(webProxy);
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
        MultiValueMap<String, String> params = request.getParams();
        List<String> params1 = params.get(paramName);
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

        MultiValueMap<String, String> params = request.getParams();
        List<String> params1 = params.get(paramNames[0]);
        assertThat(params1).containsExactly(paramValues1);
        List<String> params2 = params.get(paramNames[1]);
        assertThat(params2).containsExactly(paramValue2);
    }

    @Test
    public void shouldThrowExceptionInParamWhenRequestHasAlreadyExecuted() throws Exception {
        WebProxy webProxy = mock(WebProxy.class);
        Response response = mock(Response.class);
        when(requestExecutor.execute(webProxy)).thenReturn(response);
        request.execute(webProxy);
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

        request.expectIfUnset(condition1);
        request.expectIfUnset(condition2);
        request.expectIfUnset(condition3);

        Collection<ResponseCondition> expectations = request.getExpectations();
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
        request.expect(condition1);
        request.expect(condition2);
        InOrder inOrder = inOrder(condition1, condition2);

        request.execute(webProxy);

        inOrder.verify(condition1).match(request, response);
        inOrder.verify(condition2).match(request, response);
    }

    @Test
    public void shouldThrowExceptionInExpectWhenRequestHasAlreadyExecuted() throws Exception {
        WebProxy webProxy = mock(WebProxy.class);
        Response response = mock(Response.class);
        when(requestExecutor.execute(webProxy)).thenReturn(response);
        request.execute(webProxy);
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The test has already been executed");

        request.expect(mock(ResponseCondition.class));
    }

    @Test
    public void shouldThrowExceptionInExpectIfUnsetWhenRequestHasAlreadyExecuted() throws Exception {
        WebProxy webProxy = mock(WebProxy.class);
        Response response = mock(Response.class);
        when(requestExecutor.execute(webProxy)).thenReturn(response);
        request.execute(webProxy);
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The test has already been executed");

        request.expectIfUnset(mock(ResponseCondition.class));
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
