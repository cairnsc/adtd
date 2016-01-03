package com.thoughtworks.adtd.http.responseConditions.body;

import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.Response;
import com.thoughtworks.adtd.testutil.BasicHtml;
import com.thoughtworks.adtd.util.AssertionFailureException;
import com.thoughtworks.adtd.util.failureMessages.ShouldBeEmpty;
import com.thoughtworks.adtd.util.failureMessages.ShouldNotBeEmpty;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HasContentTests {
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
    public void shouldNotThrowExceptionWhenShouldHaveContentAndResponseHasBody() throws Exception {
        when(responseMock.getBody()).thenReturn(BasicHtml.HTML);
        HasContent condition = new HasContent(true);

        condition.match(requestMock, responseMock);
    }

    @Test
    public void shouldNotThrowExceptionWhenShouldNotHaveContentAndResponseHasNullBody() throws Exception {
        when(responseMock.getBody()).thenReturn(null);
        HasContent condition = new HasContent(false);

        condition.match(requestMock, responseMock);
    }

    @Test
    public void shouldNotThrowExceptionWhenHasShouldNotHaveContentAndResponseHasEmptyBody() throws Exception {
        when(responseMock.getBody()).thenReturn("");
        HasContent condition = new HasContent(false);

        condition.match(requestMock, responseMock);
    }

    @Test
    public void shouldThrowExceptionWhenShouldHaveContentAndResponseBodyIsNull() throws Exception {
        when(responseMock.getBody()).thenReturn(null);
        expectedException.expect(AssertionFailureException.class);
        expectedException.expectMessage(ShouldNotBeEmpty.shouldNotBeEmpty("HTTP response body", REQUEST_CONTEXT));
        HasContent condition = new HasContent(true);

        condition.match(requestMock, responseMock);
    }

    @Test
    public void shouldThrowExceptionWhenShouldHaveContentAndResponseBodyIsEmpty() throws Exception {
        when(responseMock.getBody()).thenReturn("");
        expectedException.expect(AssertionFailureException.class);
        expectedException.expectMessage(ShouldNotBeEmpty.shouldNotBeEmpty("HTTP response body", REQUEST_CONTEXT));
        HasContent condition = new HasContent(true);

        condition.match(requestMock, responseMock);
    }

    @Test
    public void shouldThrowExceptionWhenShouldNotHaveContentAndHasResponseBody() throws Exception {
        when(responseMock.getBody()).thenReturn(BasicHtml.HTML);
        expectedException.expect(AssertionFailureException.class);
        expectedException.expectMessage(ShouldBeEmpty.shouldBeEmpty("HTTP response body", REQUEST_CONTEXT));
        HasContent condition = new HasContent(false);

        condition.match(requestMock, responseMock);
    }
}