package com.thoughtworks.adtd.csrf;

import com.thoughtworks.adtd.html.Form;
import com.thoughtworks.adtd.html.FormData;
import com.thoughtworks.adtd.http.*;
import com.thoughtworks.adtd.http.responseConditions.status.HasStatusCode;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CsrfTokenSubmitRequestTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private CsrfTokenTestImpl testOrchestrator;
    private Form form;
    private FormData formData;
    private CsrfTokenSubmitRequest submitRequest;
    private Request request;
    private WebProxy webProxy;
    private Response response;

    @Test
    public void shouldPrepareRequestUsingForm() throws Exception {
        testOrchestrator = mock(CsrfTokenTestImpl.class);
        form = mock(Form.class);
        formData = mock(FormData.class);
        submitRequest = new CsrfTokenSubmitRequest(testOrchestrator, form, formData, "token");

        submitRequest.prepareRequest();

        verify(form).createRequest(submitRequest);
    }

    @Test
    public void shouldExecuteRetrieveRequestUsingWebProxy() throws Exception {
        createSubmitRequest();
        createMockedResponse(200, CsrfTokenTestImplTests.BASIC_HTML);

        Response result = submitRequest.execute(webProxy);

        assertThat(response).isEqualTo(result);
        verify(webProxy).execute(request);
    }

    @Test
    public void shouldRegisterHasStatusCodeConditionInRequestIfUnset() throws Exception {
        createSubmitRequest();
        createMockedResponse(200, CsrfTokenTestImplTests.BASIC_HTML);

        Response result = request.execute(webProxy);

        Collection<ResponseCondition> expectations = request.getExpectations();
        // REVISIT: should assert that it was added only if it was unset
        assertThat(expectations).contains(new HasStatusCode(HttpStatus.OK));
    }

    @Test
    public void shouldRetainResponse() throws Exception {
        createSubmitRequest();
        createMockedResponse(200, CsrfTokenTestImplTests.BASIC_HTML);

        Response result = request.execute(webProxy);

        Response submitRequestResponse = submitRequest.getResponse();
        assertThat(submitRequestResponse).isEqualTo(response);
    }

    @Test
    public void shouldNotifyTestOrchestratorAfterProcessingResponse() throws Exception {
        createSubmitRequest();
        createMockedResponse(200, CsrfTokenTestImplTests.BASIC_HTML);

        Response result = request.execute(webProxy);

        assertThat(response).isEqualTo(result);
        verify(testOrchestrator).notifyRequestComplete();
    }

    private void createSubmitRequest() throws Exception {
        testOrchestrator = mock(CsrfTokenTestImpl.class);
        form = mock(Form.class);
        formData = mock(FormData.class);
        submitRequest = new CsrfTokenSubmitRequest(testOrchestrator, form, formData, "token");
        request = createRequest();
        when(form.createRequest(submitRequest)).thenReturn(request);
        submitRequest.prepareRequest();
    }

    private Request createRequest() throws Exception {
        Request request = new RequestImpl(submitRequest);
        request.method("POST");
        return request;
    }

    private void createMockedResponse(int statusCode, String body) throws Exception {
        webProxy = mock(WebProxy.class);
        response = mock(Response.class);
        when(response.getStatus()).thenReturn(statusCode);
        when(response.getBody()).thenReturn(body);
        when(webProxy.execute(request)).thenReturn(response);
    }

}
