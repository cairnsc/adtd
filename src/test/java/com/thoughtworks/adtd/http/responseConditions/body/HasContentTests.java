package com.thoughtworks.adtd.http.responseConditions.body;

import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.Response;
import com.thoughtworks.adtd.testutil.BasicHtml;
import com.thoughtworks.adtd.util.AssertionFailureException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HasContentTests {
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
    public void shouldNotThrowExceptionWhenShouldHaveContentAndResponseHasBody() throws Exception {
        when(response.getBody()).thenReturn(BasicHtml.HTML);
        HasContent condition = new HasContent(true);

        condition.match(request, response);
    }

    @Test
    public void shouldNotThrowExceptionWhenShouldNotHaveContentAndResponseHasNullBody() throws Exception {
        when(response.getBody()).thenReturn(null);
        HasContent condition = new HasContent(false);

        condition.match(request, response);
    }

    @Test
    public void shouldNotThrowExceptionWhenHasShouldNotHaveContentAndResponseHasEmptyBody() throws Exception {
        when(response.getBody()).thenReturn("");
        HasContent condition = new HasContent(false);

        condition.match(request, response);
    }

    @Test
    public void shouldThrowExceptionWhenShouldHaveContentAndResponseBodyIsNull() throws Exception {
        when(response.getBody()).thenReturn(null);
        expectedException.expect(AssertionFailureException.class);
        expectedException.expectMessage("HTTP response body is empty");
        HasContent condition = new HasContent(true);

        condition.match(request, response);
    }

    @Test
    public void shouldThrowExceptionWhenShouldHaveContentAndResponseBodyIsEmpty() throws Exception {
        when(response.getBody()).thenReturn("");
        expectedException.expect(AssertionFailureException.class);
        expectedException.expectMessage("HTTP response body is empty");
        HasContent condition = new HasContent(true);

        condition.match(request, response);
    }

    @Test
    public void shouldThrowExceptionWhenShouldNotHaveContentAndHasResponseBody() throws Exception {
        when(response.getBody()).thenReturn(BasicHtml.HTML);
        expectedException.expect(AssertionFailureException.class);
        expectedException.expectMessage("HTTP response body is not empty");
        HasContent condition = new HasContent(false);

        condition.match(request, response);
    }
}