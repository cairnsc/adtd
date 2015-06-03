package com.thoughtworks.adtd.csrf;

import com.thoughtworks.adtd.http.*;
import com.thoughtworks.adtd.http.responseConditions.status.HasStatusCode;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class CsrfTokenTestImplTests {

    private static final String BASIC_HTML_BODY = "<html><body>test</body></html>";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private CsrfTokenTestImpl test;
    private WebProxy webProxy;
    private Request retrieveRequest;
    private Response response;

    @Test
    public void shouldThrowExceptionIfPrepareRetrieveInvokedMoreThanOnce() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A retrieval request has already been created for this test");
        test = new CsrfTokenTestImpl();
        test.prepareRetrieve("test", "/test");

        test.prepareRetrieve("test", "/test");
    }

    @Test
    public void shouldUseGetMethodInRetrieveRequest() {
        createTestRetrieveRequest("test", "/test");

        assertThat(retrieveRequest.getMethod()).isEqualTo("GET");
    }

    @Test
    public void shouldExecuteRetrieveRequestUsingWebProxy() throws Exception {
        createTestRetrieveRequest("test", "/test");
        createMockedResponse(200, BASIC_HTML_BODY);

        Response result = retrieveRequest.execute(webProxy);

        assertThat(response).isEqualTo(result);
        verify(webProxy).execute(retrieveRequest);
    }

    @Test
    public void shouldRegisterHasStatusCodeConditionInRetrieveRequestIfUnset() throws Exception {
        createTestRetrieveRequest("test", "/test");
        createMockedResponse(200, BASIC_HTML_BODY);

        Response result = retrieveRequest.execute(webProxy);

        Collection<ResponseCondition> expectations = retrieveRequest.getExpectations();
        // REVISIT: should assert that it was added only if it was unset
        assertThat(expectations).contains(new HasStatusCode(HttpStatus.OK));
    }

    @Test
    public void shouldExecuteSubmitRequestUsingWebProxy() {
    }

    @Test
    public void shouldThrowExceptionIfTokenWillBeExposedInRequest() {
    }

    private void createTestRetrieveRequest(String formAction, String tokenInputName) {
        test = new CsrfTokenTestImpl();
        retrieveRequest = test.prepareRetrieve(formAction, tokenInputName);
    }

    private void createMockedResponse(int statusCode, String body) throws Exception {
        webProxy = mock(WebProxy.class);
        response = mock(Response.class);
        when(response.getStatus()).thenReturn(statusCode);
        when(response.getBody()).thenReturn(body);
        when(webProxy.execute(retrieveRequest)).thenReturn(response);
    }

}
