package com.thoughtworks.adtd.http.responseConditions;

import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.Response;
import com.thoughtworks.adtd.util.AssertionFailureException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResponseStatusConditionTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private Request request;
    private Response response;

    @Before
    public void setUp() {
        request = mock(Request.class);
        response = mock(Response.class);
    }

    @Test
    public void shouldMatchStatusCodeInResponse() throws Exception {
        int statusCode = 123;
        ResponseStatusCondition condition = new ResponseStatusCondition(statusCode);
        when(response.getStatus()).thenReturn(statusCode);

        condition.match(request, response);
    }

    @Test
    public void shouldThrowExceptionWhenStatusCodeInResponseDoesNotMatch() throws Exception {
        int expectedStatusCode = 200;
        int actualStatusCode = 400;
        ResponseStatusCondition condition = new ResponseStatusCondition(expectedStatusCode);
        when(response.getStatus()).thenReturn(actualStatusCode);
        expectedException.expect(AssertionFailureException.class);
        expectedException.expectMessage("HTTP response status code: expected \"" + expectedStatusCode + "\", actual \"" + actualStatusCode + "\"");

        condition.match(request, response);
    }

}
