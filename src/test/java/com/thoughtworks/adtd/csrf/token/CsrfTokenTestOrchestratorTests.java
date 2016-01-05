package com.thoughtworks.adtd.csrf.token;

import com.thoughtworks.adtd.http.RequestInfo;
import com.thoughtworks.adtd.http.RequestParameters;
import com.thoughtworks.adtd.util.AssertionFailureException;
import com.thoughtworks.adtd.util.failureMessages.ShouldHaveNumElements;
import com.thoughtworks.adtd.util.failureMessages.ShouldNotBeEmpty;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CsrfTokenTestOrchestratorTests {
    public static final String TOKEN_INPUT_NAME = "test";
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private CsrfTokenTestOrchestrator iterator;
    private RequestInfo requestInfoMock;
    private RequestParameters requestParameters;
    private ResponseValidator responseValidatorMock;

    @Before
    public void setUp() {
        requestInfoMock = mock(RequestInfo.class);
        requestParameters = new RequestParameters();
        when(requestInfoMock.getRequestParameters()).thenReturn(requestParameters);
        responseValidatorMock = mock(ResponseValidator.class);
    }

    @Test
    public void shouldMarkRequestParametersAsImmutable() throws Exception {
        createIterator();

        verify(requestInfoMock).setImmutable();
    }

    @Test
    public void shouldThrowExceptionIfRequestHasNoCsrfTokens() throws Exception {
        expectedException.expect(AssertionFailureException.class);
        expectedException.expectMessage(ShouldHaveNumElements.shouldHaveNumElements(
                "CSRF token named \"" + TOKEN_INPUT_NAME + "\"", 1, 0
        ));

        iterator = new CsrfTokenTestOrchestrator(requestInfoMock, responseValidatorMock, TOKEN_INPUT_NAME);
    }

    @Test
    public void shouldThrowExceptionIfRequestHasMultipleCsrfTokens() throws Exception {
        requestParameters.param(TOKEN_INPUT_NAME, "1");
        requestParameters.param(TOKEN_INPUT_NAME, "2");
        expectedException.expect(AssertionFailureException.class);
        expectedException.expectMessage(ShouldHaveNumElements.shouldHaveNumElements(
                "CSRF token named \"" + TOKEN_INPUT_NAME + "\"", 1, 2
        ));

        iterator = new CsrfTokenTestOrchestrator(requestInfoMock, responseValidatorMock, TOKEN_INPUT_NAME);
    }

    @Test
    public void shouldThrowExceptionIfParamHasMultipleValues() throws Exception {
        requestParameters.param(TOKEN_INPUT_NAME, "1", "2");
        expectedException.expect(AssertionFailureException.class);
        expectedException.expectMessage(ShouldHaveNumElements.shouldHaveNumElements(
                "CSRF token at request parameter index 0", 1, 2
        ));

        iterator = new CsrfTokenTestOrchestrator(requestInfoMock, responseValidatorMock, TOKEN_INPUT_NAME);
    }

    @Test
    public void shouldThrowExceptionIfParamValueIsEmpty() throws Exception {
        requestParameters.param(TOKEN_INPUT_NAME, "  ");
        expectedException.expect(AssertionFailureException.class);
        expectedException.expectMessage(ShouldNotBeEmpty.shouldNotBeEmpty("CSRF token at request parameter index 0"));

        iterator = new CsrfTokenTestOrchestrator(requestInfoMock, responseValidatorMock, TOKEN_INPUT_NAME);
    }

    @Test
    public void shouldThrowExceptionOnRemove() throws Exception {
        createIterator();
        expectedException.expect(UnsupportedOperationException.class);

        iterator.remove();
    }

    @Test
    public void shouldExhaustIterator() throws Exception {
        createIterator();
        int count = iterator.count();
        assertThat(count).isGreaterThan(0);

        int idx;
        for (idx = 0; idx < count; idx++) {
            CsrfTokenTest next = iterator.next();
            assertThat(next).isNotNull();
        }

        assertThat(idx).isEqualTo(count);
        assertThat(iterator.hasNext()).isFalse();
    }

    private void createIterator() throws Exception {
        requestParameters.param(TOKEN_INPUT_NAME, "1");
        iterator = new CsrfTokenTestOrchestrator(requestInfoMock, responseValidatorMock, TOKEN_INPUT_NAME);
    }
}
