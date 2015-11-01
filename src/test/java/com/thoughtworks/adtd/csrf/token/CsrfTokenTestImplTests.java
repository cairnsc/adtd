package com.thoughtworks.adtd.csrf.token;

import com.thoughtworks.adtd.csrf.token.strategies.TestStrategy;
import com.thoughtworks.adtd.html.Form;
import com.thoughtworks.adtd.html.FormData;
import com.thoughtworks.adtd.http.*;
import com.thoughtworks.adtd.http.responseConditions.status.HasStatusCode;
import com.thoughtworks.adtd.testutil.BasicHtml;
import com.thoughtworks.adtd.testutil.BasicHtmlForm;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class CsrfTokenTestImplTests {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private WebProxy webProxy;
    private TestStrategy testStrategy;
    private Form form;
    private FormData formData;
    private ResponseValidator responseValidator;
    private CsrfTokenTestImpl test;
    private Request request;
    private Response response;

    @Test
    public void shouldThrowExceptionIfPrepareInvokedMoreThanOnce() throws Exception {
        createTest(BasicHtmlForm.HIDDEN_TOKEN_NAME);
        test.prepare();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A request has already been prepared for this test");

        test.prepare();
    }

    @Test
    public void shouldPrepareRequest() throws Exception {
        createTest(BasicHtmlForm.HIDDEN_TOKEN_NAME);

        test.prepare();

        InOrder inOrder = inOrder(form, testStrategy, formData);
        inOrder.verify(form).createRequest(test);
        inOrder.verify(testStrategy).mutateFormData(formData);
        inOrder.verify(formData).setImmutable();
        inOrder.verify(formData).setRequestParams(request);
    }

    @Test
    public void shouldExecuteRequestUsingWebProxy() throws Exception {
        createTest(BasicHtmlForm.HIDDEN_TOKEN_NAME);

        Response result = prepareAndExecuteRequest(HttpStatus.OK.getStatusCode(), BasicHtml.HTML);

        assertThat(result).isEqualTo(response);
        verify(webProxy).execute(request);
    }

    @Test
    public void shouldRegisterHasStatusCodeConditionInRequestIfUnsetDuringExecute() throws Exception {
        createTest(BasicHtmlForm.HIDDEN_TOKEN_NAME);

        prepareAndExecuteRequest(HttpStatus.OK.getStatusCode(), BasicHtml.HTML);

        Collection<ResponseCondition> expectations = request.getExpectations();
        // REVISIT: should assert that it was added only if it was unset
        assertThat(expectations).contains(new HasStatusCode(HttpStatus.OK));
    }

    @Test
    public void shouldThrowExceptionInAssertResponseBeforeRequestCreated() throws Exception {
        createTest(BasicHtmlForm.HIDDEN_TOKEN_NAME);
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A request must first be prepared and executed for this test");

        test.assertResponse();
    }

    @Test
    public void shouldThrowExceptionInAssertResponseBeforeRequestExecuted() throws Exception {
        createTest(BasicHtmlForm.HIDDEN_TOKEN_NAME);
        test.prepare();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A request must first be prepared and executed for this test");

        test.assertResponse();
    }

    @Test
    public void shouldInvokeValidatorInAssertResponse() throws Exception {
        createTest(BasicHtmlForm.HIDDEN_TOKEN_NAME);
        prepareAndExecuteRequest(200, BasicHtml.HTML);

        test.assertResponse();

        verify(responseValidator).validate(request, response);
    }

    private void createTest(String tokenInputName) throws Exception {
        testStrategy = mock(TestStrategy.class);
        form = mock(Form.class);
        formData = mock(FormData.class);
        responseValidator = mock(ResponseValidator.class);
        test = new CsrfTokenTestImpl(testStrategy, tokenInputName, form, formData, responseValidator);
        request = createRequest();
        when(form.createRequest(test)).thenReturn(request);
    }

    private Request createRequest() throws Exception {
        Request request = new RequestImpl(test);
        request.method("POST");
        return request;
    }

    private Response prepareAndExecuteRequest(int statusCode, String body) throws Exception {
        Request preparedRequest = test.prepare();
        assertThat(preparedRequest).isEqualTo(request);
        response = createMockedResponse(preparedRequest, statusCode, body);
        return request.execute(webProxy);
    }

    private Response createMockedResponse(Request request, int statusCode, String body) throws Exception {
        webProxy = mock(WebProxy.class);
        Response response = mock(Response.class);
        when(response.getStatus()).thenReturn(statusCode);
        when(response.getBody()).thenReturn(body);
        when(webProxy.execute(request)).thenReturn(response);
        return response;
    }
}
