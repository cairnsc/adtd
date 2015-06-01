package com.thoughtworks.adtd.http;

import com.thoughtworks.adtd.util.MultiValueMap;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
    public void shouldSetUri() {
        String uri = "http://adtd/test";

        Request retVal = request.uri(uri);
        assertThat(retVal).isEqualTo(request);

        assertThat(request.getUri()).isEqualTo(uri);
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
    public void shouldExecuteAgainstRequestSubject() throws Exception {
        WebProxy webProxy = mock(WebProxy.class);
        Response response = mock(Response.class);
        when(requestExecutor.execute(webProxy)).thenReturn(response);

        Response result = request.execute(webProxy);

        verify(requestExecutor).execute(webProxy);
        assertThat(result).isEqualTo(response);
    }

    @Test
    public void shouldThrowExceptionWhenRequestHasAlreadyExecuted() throws Exception {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The test has already been executed");
        WebProxy webProxy = mock(WebProxy.class);
        when(requestExecutor.execute(webProxy)).thenReturn(mock(Response.class));
        request.execute(webProxy);

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
    public void shouldExpectStatusCode200ByDefault() {
        assertThat(request.getExpectedStatusCode()).isEqualTo(HttpStatus.OK.getStatusCode());
    }

    @Test
    public void shouldSetExpectedStatusCode() {
        int statusCode = 999;
        request.expectStatusCode(statusCode);

        assertThat(request.getExpectedStatusCode()).isEqualTo(statusCode);
    }

}
