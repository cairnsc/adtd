package com.thoughtworks.adtd.http;

import com.thoughtworks.adtd.html.Form;
import com.thoughtworks.adtd.html.FormField;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.thoughtworks.adtd.testutil.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestInfoTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldSetImmutable() {
        RequestInfo requestInfo = new RequestInfo("GET", "/");

        requestInfo.setImmutable();

        assertThat(requestInfo.isMutable()).isFalse();
        assertThat(requestInfo.getRequestParameters().isMutable()).isFalse();
    }

    @Test
    public void shouldCreateRequestInfoWithMethodAndUri() {
        String method = "test";
        String uri = "/test";

        RequestInfo requestInfo = new RequestInfo(method, uri);

        assertThat(requestInfo.getMethod()).isEqualTo(method);
        assertThat(requestInfo.getUri()).isEqualTo(uri);
        assertThat(requestInfo.getRequestParameters().size()).isEqualTo(0);
    }

    @Test
    public void shouldCreateRequestInfoWithMethodAndUriAndFields() {
        String method = "test";
        String uri = "/test";
        List<FormField> formFields = newArrayList();
        formFields.add(new FormField("a", "b"));
        formFields.add(new FormField("c", "d"));

        RequestInfo requestInfo = new RequestInfo(method, uri, formFields);

        assertThat(requestInfo.getMethod()).isEqualTo(method);
        assertThat(requestInfo.getUri()).isEqualTo(uri);
        assertThat(requestInfo.getRequestParameters()).isDerivedFrom(formFields);
    }

    @Test
    public void shouldCreateRequestInfoFromForm() throws Exception {
        Form formMock = mock(Form.class);
        String action = "/test";
        when(formMock.getAction()).thenReturn(action);
        String method = "test";
        when(formMock.getMethod()).thenReturn(method);
        List<FormField> formFields = newArrayList();
        formFields.add(new FormField("a", "b"));
        formFields.add(new FormField("c", "d"));
        when(formMock.getFormFields()).thenReturn(formFields);

        RequestInfo requestInfo = new RequestInfo(formMock);

        assertThat(requestInfo.getMethod()).isEqualTo(method);
        assertThat(requestInfo.getUri()).isEqualTo(action);
        assertThat(requestInfo.getRequestParameters()).isDerivedFrom(formMock);
    }

    @Test
    public void shouldThrowExceptionInSetMethodWhenRequestInfoIsImmutable() {
        RequestInfo requestInfo = new RequestInfo("POST", "/");
        requestInfo.setImmutable();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("RequestInfo is immutable");

        requestInfo.setMethod("GET");
    }

    @Test
    public void shouldThrowExceptionInSetUriWhenRequestInfoIsImmutable() {
        RequestInfo requestInfo = new RequestInfo("POST", "/");
        requestInfo.setImmutable();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("RequestInfo is immutable");

        requestInfo.setUri("/test");
    }

    @Test
    public void shouldCreateRequest() throws Exception {
        List<FormField> formFields = newArrayList();
        formFields.add(new FormField("a", "b"));
        formFields.add(new FormField("c", "d"));
        RequestInfo requestInfo = new RequestInfo("POST", "/", formFields);

        Request request = requestInfo.createRequest(mock(RequestExecutor.class), null);

        assertThat(request.getMethod()).isEqualTo(requestInfo.getMethod());
        assertThat(request.getUri()).isEqualTo(requestInfo.getUri());
        assertThat(request.getParams()).isDerivedFrom(formFields);
    }
}