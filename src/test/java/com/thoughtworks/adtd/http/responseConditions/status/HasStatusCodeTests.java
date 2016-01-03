package com.thoughtworks.adtd.http.responseConditions.status;

import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.Response;
import com.thoughtworks.adtd.util.AssertionFailureException;
import com.thoughtworks.adtd.util.failureMessages.ShouldHaveValue;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HasStatusCodeTests {
    public static final String REQUEST_CONTEXT = "some context";
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private Request requestMock;
    private Response responseMock;

    @Before
    public void setUp() {
        requestMock = mock(Request.class);
        when(requestMock.getContext()).thenReturn(REQUEST_CONTEXT);
        responseMock = mock(Response.class);
    }

    @Test
    public void shouldMatchStatusCodeInResponse() throws Exception {
        int statusCode = 123;
        HasStatusCode condition = new HasStatusCode(statusCode);
        when(responseMock.getStatus()).thenReturn(statusCode);

        condition.match(requestMock, responseMock);
    }

    @Test
    public void shouldThrowExceptionWhenStatusCodeInResponseDoesNotMatch() throws Exception {
        int expectedStatusCode = 200;
        int actualStatusCode = 400;
        HasStatusCode condition = new HasStatusCode(expectedStatusCode);
        when(responseMock.getStatus()).thenReturn(actualStatusCode);
        expectedException.expect(AssertionFailureException.class);
        expectedException.expectMessage(ShouldHaveValue.shouldHaveValue("HTTP response status code", expectedStatusCode, actualStatusCode, REQUEST_CONTEXT));

        condition.match(requestMock, responseMock);
    }
}
