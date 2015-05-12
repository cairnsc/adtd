package com.thoughtworks.adtd.xss;

import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.Response;
import com.thoughtworks.adtd.http.WebProxy;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class XssTestImplTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldReturnTestString() {
        String testPattern = "<script>";
        XssTestImpl test = new XssTestImpl(testPattern);

        assertThat(test.getTestPattern()).isEqualTo(testPattern);
    }

    @Test
    public void shouldMatchPattern() {
        String testPattern = "<script>adtd();</script>";
        XssTestImpl test = new XssTestImpl(testPattern);
        String content = "<html><body>" + testPattern + "</body></html>";

        boolean matches = test.matches(content);

        assertThat(matches).isTrue();
    }

    @Test
    public void shouldMatchPatternCaseInsensitive() {
        String testPattern = "<script>adtd();</script>";
        XssTestImpl test = new XssTestImpl(testPattern);
        String content = "<html><body>" + testPattern.toUpperCase() + "</body></html>";

        boolean matches = test.matches(content);

        assertThat(matches).isTrue();
    }

    @Test
    public void shouldThrowExceptionIfPrepareInvokedMoreThanOnce() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A test request has already been created for this test");
        XssTestImpl test = new XssTestImpl("");

        test.prepare();

        test.prepare();
    }

    @Test
    public void shouldExecuteRequestUsingWebProxy() throws Exception {
        XssTestImpl test = new XssTestImpl("<script>adtd();</script>");
        WebProxy webProxy = mock(WebProxy.class);
        Request request = test.prepare();
        Response response = mock(Response.class);
        when(webProxy.execute(request)).thenReturn(response);
        Response result = request.execute(webProxy);

        assertThat(response).isEqualTo(result);
        verify(webProxy).execute(request);
    }

    @Test
    public void shouldThrowExceptionWhenNoResponseIsSetInAssertResponse() throws Exception {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The test has not yet been executed");
        XssTestImpl test = new XssTestImpl("");
        test.prepare().execute(mock(WebProxy.class));

        test.assertResponse();
    }

}
