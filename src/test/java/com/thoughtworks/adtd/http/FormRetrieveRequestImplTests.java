package com.thoughtworks.adtd.http;

import com.thoughtworks.adtd.csrf.token.CsrfTokenTestImplTests;
import com.thoughtworks.adtd.html.Form;
import com.thoughtworks.adtd.http.responseConditions.status.HasStatusCode;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class FormRetrieveRequestImplTests {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private FormRetrieveRequestImpl retrieveRequest;
    private WebProxy webProxy;
    private Request request;
    private Response response;

    @Test
    public void shouldThrowExceptionIfPrepareInvokedMoreThanOnce() {
        createFormRetrieveRequest("test");
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A request has already been prepared");

        retrieveRequest.prepare();
    }

    @Test
    public void shouldUseGetMethodByDefaultInRequest() {
        createFormRetrieveRequest("test");

        assertThat(request.getMethod()).isEqualTo("GET");
    }

    @Test
    public void shouldThrowExceptionIfFormDataNotAvailable() throws Exception {
        createFormRetrieveRequest("test");
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A request must first be prepared and executed");

        retrieveRequest.getForm();
    }

    @Test
    public void shouldGetForm() throws Exception {
        createFormRetrieveRequest("test");
        createMockedResponse(HttpStatus.OK.getStatusCode(), CsrfTokenTestImplTests.BASIC_FORM_BODY);
        request.execute(webProxy);

        Form form = retrieveRequest.getForm();

        assertThat(form).isNotNull();
    }

    @Test
    public void shouldExecuteRetrieveRequestUsingWebProxy() throws Exception {
        createFormRetrieveRequest("test");
        createMockedResponse(HttpStatus.OK.getStatusCode(), CsrfTokenTestImplTests.BASIC_FORM_BODY);

        Response result = request.execute(webProxy);

        assertThat(response).isEqualTo(result);
        verify(webProxy).execute(request);
    }

    @Test
    public void shouldRegisterHasStatusCodeConditionInRequestIfUnset() throws Exception {
        createFormRetrieveRequest("test");
        createMockedResponse(HttpStatus.OK.getStatusCode(), CsrfTokenTestImplTests.BASIC_FORM_BODY);

        Response result = request.execute(webProxy);

        Collection<ResponseCondition> expectations = request.getExpectations();
        // REVISIT: should assert that it was added only if it was unset
        assertThat(expectations).contains(new HasStatusCode(HttpStatus.OK));
    }

//    @Test
//    public void shouldThrowExceptionIfTokenWillBeExposedInRequest() throws Exception {
//        String formAction = "test";
//        String tokenInputName = "token";
//        String tokenHtml = "<input type=\"hidden\" name=\"" + tokenInputName + "\" value=\"123\" method=\"get\">";
//        String html = "<html><body><form action=\"" + formAction + "\">" + tokenHtml + "</form></body></html>";
//        createFormRetrieveRequest(formAction, tokenInputName);
//        createMockedResponse(HttpStatus.OK.getStatusCode(), html);
//        expectedException.expect(ElementTypeException.class);
//        expectedException.expectMessage("Element input with name=\"" + tokenInputName + "\" has method=\"GET\", expected \"POST\"");
//
//        request.execute(webProxy);
//    }

//    @Test
//    public void shouldThrowExceptionIfTokenInputValueIsNull() throws Exception {
//        String formAction = "test";
//        String tokenInputName = "token";
//        String tokenHtml = "<input type=\"hidden\" name=\"" + tokenInputName + "\">";
//        String html = "<html><body><form action=\"" + formAction + "\" method=\"post\">" + tokenHtml + "</form></body></html>";
//        createFormRetrieveRequest(formAction, tokenInputName);
//        createMockedResponse(HttpStatus.OK.getStatusCode(), html);
//        expectedException.expect(ElementAttributeException.class);
//        expectedException.expectMessage("Element input with name=\"" + tokenInputName + "\" has no value");
//
//        request.execute(webProxy);
//    }
//
//    @Test
//    public void shouldThrowExceptionIfTokenInputValueIsEmpty() throws Exception {
//        String formAction = "test";
//        String tokenInputName = "token";
//        String tokenHtml = "<input type=\"hidden\" name=\"" + tokenInputName + "\" value=\"  \">";
//        String html = "<html><body><form action=\"" + formAction + "\" method=\"post\">" + tokenHtml + "</form></body></html>";
//        createFormRetrieveRequest(formAction, tokenInputName);
//        createMockedResponse(HttpStatus.OK.getStatusCode(), html);
//        expectedException.expect(ElementAttributeException.class);
//        expectedException.expectMessage("Element input with name=\"" + tokenInputName + "\" has no value");
//
//        request.execute(webProxy);
//    }
//
//    @Test
//    public void shouldThrowExceptionIfInputTokenIsNotHidden() throws Exception {
//        String formAction = "test";
//        String tokenInputName = "token";
//        String inputType = "foo";
//        String tokenHtml = "<input type=\"" + inputType + "\" name=\"" + tokenInputName + "\" value=\"123\">";
//        String html = "<html><body><form action=\"" + formAction + "\" method=\"post\">" + tokenHtml + "</form></body></html>";
//        createFormRetrieveRequest(formAction, tokenInputName);
//        createMockedResponse(HttpStatus.OK.getStatusCode(), html);
//        expectedException.expect(ElementTypeException.class);
//        expectedException.expectMessage("Element input with name=\"" + tokenInputName + "\" has type=\"" + inputType + "\", expected \"hidden\"");
//
//        request.execute(webProxy);
//    }

    private void createFormRetrieveRequest(String formAction) {
        retrieveRequest = new FormRetrieveRequestImpl(formAction);
        request = retrieveRequest.prepare();
    }

    private void createMockedResponse(int statusCode, String body) throws Exception {
        webProxy = mock(WebProxy.class);
        response = mock(Response.class);
        when(response.getStatus()).thenReturn(statusCode);
        when(response.getBody()).thenReturn(body);
        when(webProxy.execute(request)).thenReturn(response);
    }
}
