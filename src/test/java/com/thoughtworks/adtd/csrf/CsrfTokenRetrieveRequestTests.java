package com.thoughtworks.adtd.csrf;

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

public class CsrfTokenRetrieveRequestTests {

    private static final String BASIC_HTML_BODY = "<html><body>test</body></html>";
    private static final String BASIC_FORM_BODY = "<html><body><form action=\"test\" method=\"post\"><input type=\"hidden\" name=\"test\" value=\"xyz\"></form></html>";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private CsrfTokenRetrieveRequest retrieveRequest;
    private WebProxy webProxy;
    private Request request;
    private Response response;

    @Test
    public void shouldUseGetMethodByDefaultInRetrieveRequest() {
        createRequest("test", "test");

        assertThat(request.getMethod()).isEqualTo("GET");
    }

    @Test
    public void shouldExecuteRetrieveRequestUsingWebProxy() throws Exception {
        createRequest("test", "test");
        createMockedResponse(200, BASIC_FORM_BODY);

        Response result = request.execute(webProxy);

        assertThat(response).isEqualTo(result);
        verify(webProxy).execute(request);
    }

    @Test
    public void shouldRegisterHasStatusCodeConditionInRequestIfUnset() throws Exception {
        createRequest("test", "test");
        createMockedResponse(200, BASIC_FORM_BODY);

        Response result = request.execute(webProxy);

        Collection<ResponseCondition> expectations = request.getExpectations();
        // REVISIT: should assert that it was added only if it was unset
        assertThat(expectations).contains(new HasStatusCode(HttpStatus.OK));
    }

    @Test
    public void shouldThrowExceptionIfFormElementNotFound() throws Exception {
        String formAction = "test";
        createRequest(formAction, "/test");
        createMockedResponse(200, BASIC_HTML_BODY);
        expectedException.expect(ElementCountException.class);
        expectedException.expectMessage("Expected 1 form elements with action=\"" + formAction + "\", found 0");

        request.execute(webProxy);
    }

    @Test
    public void shouldThrowExceptionIfMultipleFormElementsFound() throws Exception {
        String formAction = "test";
        String formHtml = "<form action=\"" + formAction + "\"></form>";
        String html = "<html><body>" + formHtml + formHtml + "</body></html>";
        createRequest(formAction, "test");
        createMockedResponse(200, html);
        expectedException.expect(ElementCountException.class);
        expectedException.expectMessage("Expected 1 form elements with action=\"" + formAction + "\", found 2");

        request.execute(webProxy);
    }

    @Test
    public void shouldThrowExceptionIfTokenInputNotFound() throws Exception {
        String formAction = "test";
        String tokenInputName = "token";
        String html = "<html><body><form action=\"" + formAction + "\"><input name=\"a\"></form></body></html>";
        createRequest(formAction, tokenInputName);
        createMockedResponse(200, html);
        expectedException.expect(ElementCountException.class);
        expectedException.expectMessage("Expected 1 input elements with name=\"" + tokenInputName + "\", found 0");

        request.execute(webProxy);
    }

    @Test
    public void shouldThrowExceptionIfTokenInputValueIsNull() throws Exception {
        String formAction = "test";
        String tokenInputName = "token";
        String tokenHtml = "<input type=\"hidden\" name=\"" + tokenInputName + "\">";
        String html = "<html><body><form action=\"" + formAction + "\">" + tokenHtml + "</form></body></html>";
        createRequest(formAction, tokenInputName);
        createMockedResponse(200, html);
        expectedException.expect(ElementAttributeException.class);
        expectedException.expectMessage("Element input with name=\"" + tokenInputName + "\" has no value");

        request.execute(webProxy);
    }

    @Test
    public void shouldThrowExceptionIfTokenInputValueIsEmpty() throws Exception {
        String formAction = "test";
        String tokenInputName = "token";
        String tokenHtml = "<input type=\"hidden\" name=\"" + tokenInputName + "\" value=\"  \">";
        String html = "<html><body><form action=\"" + formAction + "\">" + tokenHtml + "</form></body></html>";
        createRequest(formAction, tokenInputName);
        createMockedResponse(200, html);
        expectedException.expect(ElementAttributeException.class);
        expectedException.expectMessage("Element input with name=\"" + tokenInputName + "\" has no value");

        request.execute(webProxy);
    }

    @Test
    public void shouldThrowExceptionIfMultipleTokenInputsFound() throws Exception {
        String formAction = "test";
        String tokenInputName = "token";
        String tokenHtml = "<input type=\"hidden\" name=\"" + tokenInputName + "\" value=\"abc123\">";
        String html = "<html><body><form action=\"" + formAction + "\">" + tokenHtml + tokenHtml + "</form></body></html>";
        createRequest(formAction, tokenInputName);
        createMockedResponse(200, html);
        expectedException.expect(ElementCountException.class);
        expectedException.expectMessage("Expected 1 input elements with name=\"" + tokenInputName + "\", found 2");

        request.execute(webProxy);
    }

    @Test
    public void shouldThrowExceptionIfInputTokenIsNotHidden() throws Exception {
        String formAction = "test";
        String tokenInputName = "token";
        String inputType = "foo";
        String tokenHtml = "<input type=\"" + inputType + "\" name=\"" + tokenInputName + "\" value=\"123\">";
        String html = "<html><body><form action=\"" + formAction + "\">" + tokenHtml + "</form></body></html>";
        createRequest(formAction, tokenInputName);
        createMockedResponse(200, html);
        expectedException.expect(ElementTypeException.class);
        expectedException.expectMessage("Element input with name=\"" + tokenInputName + "\" has type=\"" + inputType + "\", expected \"hidden\"");

        request.execute(webProxy);
    }

    @Test
    public void shouldThrowExceptionIfTokenWillBeExposedInRequest() throws Exception {
        String formAction = "test";
        String tokenInputName = "token";
        String tokenHtml = "<input type=\"hidden\" name=\"" + tokenInputName + "\" value=\"123\" method=\"get\">";
        String html = "<html><body><form action=\"" + formAction + "\">" + tokenHtml + "</form></body></html>";
        createRequest(formAction, tokenInputName);
        createMockedResponse(200, html);
        expectedException.expect(ElementTypeException.class);
        expectedException.expectMessage("Element input with name=\"" + tokenInputName + "\" has method=\"\", expected \"POST\"");

        request.execute(webProxy);
    }

    private void createRequest(String formAction, String tokenInputName) {
        retrieveRequest = new CsrfTokenRetrieveRequest(formAction, tokenInputName);
        request = retrieveRequest.getRequest();
    }

    private void createMockedResponse(int statusCode, String body) throws Exception {
        webProxy = mock(WebProxy.class);
        response = mock(Response.class);
        when(response.getStatus()).thenReturn(statusCode);
        when(response.getBody()).thenReturn(body);
        when(webProxy.execute(request)).thenReturn(response);
    }

}
