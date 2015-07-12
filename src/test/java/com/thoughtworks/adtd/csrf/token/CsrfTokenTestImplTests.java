package com.thoughtworks.adtd.csrf.token;

import com.thoughtworks.adtd.csrf.token.CsrfTokenTestImpl;
import com.thoughtworks.adtd.html.FormData;
import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.Response;
import com.thoughtworks.adtd.http.ResponseValidator;
import com.thoughtworks.adtd.http.WebProxy;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CsrfTokenTestImplTests {

    public static final String BASIC_HTML = "<html></html>";
    public static final String BASIC_FORM_BODY = "<html><body><form action=\"test\" method=\"post\"><input type=\"hidden\" name=\"test\" value=\"xyz\"></form></html>";
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private CsrfTokenTestImpl test;
    private WebProxy webProxy;
    private Request retrieveRequest;
    private Response retrieveResponse;
    private Request submitRequest;
    private Response submitResponse;

    @Test
    public void shouldThrowExceptionIfPrepareRetrieveInvokedMoreThanOnce() {
        test = new CsrfTokenTestImpl();
        test.prepareRetrieve("test", "test");
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A retrieve request has already been prepared for this test");

        test.prepareRetrieve("test", "test");
    }

    @Test
    public void shouldGetFormDataAfterPrepareCompletes() throws Exception {
        test = new CsrfTokenTestImpl();
        prepareAndExecuteRetrieveRequest("test", "test", 200, BASIC_FORM_BODY);
        test.prepareSubmit();

        FormData formData = test.getFormData();

        assertThat(formData).isNotNull();
    }

    @Test
    public void shouldThrowExceptionIfFormDataNotAvailable() throws Exception {
        test = new CsrfTokenTestImpl();
        expectedException.expectMessage("A retrieve request must first be executed for this test");

        test.getFormData();
    }

    @Test
    public void shouldThrowExceptionIfPrepareSubmitInvokedBeforeRetrieveRequestCreated() throws Exception {
        test = new CsrfTokenTestImpl();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A retrieve request must first be executed for this test");

        test.prepareSubmit();
    }

    @Test
    public void shouldThrowExceptionIfPrepareSubmitInvokedBeforeRetrieveRequestExecuted() throws Exception {
        test = new CsrfTokenTestImpl();
        test.prepareRetrieve("test", "test");
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A retrieve request must first be executed for this test");

        test.prepareSubmit();
    }

    @Test
    public void shouldThrowExceptionIfPrepareSubmitInvokedMoreThanOnce() throws Exception {
        test = new CsrfTokenTestImpl();
        prepareAndExecuteRetrieveRequest("test", "test", 200, BASIC_FORM_BODY);
        test.prepareSubmit();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A submit request has already been prepared for this test");

        test.prepareSubmit();
    }

    @Test
    public void shouldThrowExceptionInAssertResponseBeforeSubmitRequestCreated() throws Exception {
        test = new CsrfTokenTestImpl();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A submit request must first be executed for this test");

        test.assertResponse(mock(ResponseValidator.class));
    }

    @Test
    public void shouldThrowExceptionInAssertResponseBeforeSubmitRequestExecuted() throws Exception {
        test = new CsrfTokenTestImpl();
        prepareAndExecuteRetrieveRequest("test", "test", 200, BASIC_FORM_BODY);
        test.prepareSubmit();
        ResponseValidator responseValidator = mock(ResponseValidator.class);
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A submit request must first be executed for this test");

        test.assertResponse(responseValidator);
    }

    @Test
    public void shouldInvokeValidatorInAssertResponse() throws Exception {
        test = new CsrfTokenTestImpl();
        prepareAndExecuteRetrieveRequest("test", "test", 200, BASIC_FORM_BODY);
        prepareAndExecuteSubmitRequest(200, BASIC_HTML);
        ResponseValidator responseValidator = mock(ResponseValidator.class);

        test.assertResponse(responseValidator);

        verify(responseValidator).validate(submitRequest, submitResponse);
    }

    private Response createMockedResponse(Request request, int statusCode, String body) throws Exception {
        webProxy = mock(WebProxy.class);
        Response response = mock(Response.class);
        when(response.getStatus()).thenReturn(statusCode);
        when(response.getBody()).thenReturn(body);
        when(webProxy.execute(request)).thenReturn(response);
        return response;
    }

    private void prepareAndExecuteRetrieveRequest(String formAction, String tokenInputName, int statusCode, String basicFormBody) throws Exception {
        retrieveRequest = test.prepareRetrieve(formAction, tokenInputName);
        retrieveResponse = createMockedResponse(retrieveRequest, statusCode, basicFormBody);
        retrieveRequest.execute(webProxy);
    }

    private void prepareAndExecuteSubmitRequest(int statusCode, String body) throws Exception {
        submitRequest = test.prepareSubmit();
        submitResponse = createMockedResponse(submitRequest, statusCode, body);
        submitRequest.execute(webProxy);
    }

}
