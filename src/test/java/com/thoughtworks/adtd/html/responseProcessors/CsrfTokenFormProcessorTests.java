package com.thoughtworks.adtd.html.responseProcessors;

import com.thoughtworks.adtd.html.*;
import com.thoughtworks.adtd.html.failureMessages.ShouldNotHaveAttributeValue;
import com.thoughtworks.adtd.html.failureMessages.ShouldUseHttpMethod;
import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.Response;
import com.thoughtworks.adtd.util.AssertionFailureException;
import com.thoughtworks.adtd.util.failureMessages.ShouldNotBeEmpty;
import org.jsoup.nodes.Element;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collections;

import static com.thoughtworks.adtd.util.SelectorStringBuilder.elementSelectorWithAttribute;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CsrfTokenFormProcessorTests {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private final FormResponseProcessor formResponseProcessor = mock(FormResponseProcessor.class);
    private FormField formField;

    @Test
    public void shouldThrowExceptionIfTokenWillBeExposedInRequest() throws Exception {
        String tokenInputName = "token";
        mockFormAndElement("GET", tokenInputName, "abc123", "hidden");
        CsrfTokenFormProcessor processor = new CsrfTokenFormProcessor(formResponseProcessor, tokenInputName);
        expectedException.expect(AssertionFailureException.class);
        expectedException.expectMessage(ShouldUseHttpMethod.shouldUseHttpMethod("form containing CSRF token", "POST", "GET", "Using the GET method may lead to leaking the CSRF token in the URL"));

        processor.process(mock(Request.class), mock(Response.class));
    }

    @Test
    public void shouldThrowExceptionIfTokenInputValueIsNull() throws Exception {
        String tokenInputName = "token";
        mockFormAndElement("POST", tokenInputName, null, "hidden");
        CsrfTokenFormProcessor processor = new CsrfTokenFormProcessor(formResponseProcessor, tokenInputName);
        expectedException.expect(AssertionFailureException.class);
        expectedException.expectMessage(ShouldNotBeEmpty.shouldNotBeEmpty("CSRF token input"));

        processor.process(mock(Request.class), mock(Response.class));
    }

    @Test
    public void shouldThrowExceptionIfTokenInputValueIsEmpty() throws Exception {
        String tokenInputName = "token";
        mockFormAndElement("POST", tokenInputName, " \t", "hidden");
        CsrfTokenFormProcessor processor = new CsrfTokenFormProcessor(formResponseProcessor, tokenInputName);
        expectedException.expect(AssertionFailureException.class);
        expectedException.expectMessage(ShouldNotBeEmpty.shouldNotBeEmpty("CSRF token input"));

        processor.process(mock(Request.class), mock(Response.class));
    }

    @Test
    public void shouldThrowExceptionIfInputTokenIsNotHidden() throws Exception {
        String tokenInputName = "token";
        String tokenInputType = "text";
        mockFormAndElement("POST", tokenInputName, "abc123", tokenInputType);
        CsrfTokenFormProcessor processor = new CsrfTokenFormProcessor(formResponseProcessor, tokenInputName);
        expectedException.expect(AssertionFailureException.class);
        expectedException.expectMessage(ShouldNotHaveAttributeValue.shouldNotHaveAttribute("form input containing CSRF token", "type", "hidden", tokenInputType));

        processor.process(mock(Request.class), mock(Response.class));

    }

    @Test
    public void shouldMarkFormFieldPropertyAsCsrfToken() throws Exception {
        String tokenInputName = "token";
        mockFormAndElement("POST", tokenInputName, "abc123", "hidden");
        CsrfTokenFormProcessor processor = new CsrfTokenFormProcessor(formResponseProcessor, tokenInputName);

        processor.process(mock(Request.class), mock(Response.class));

        assertThat(formField.getProperties()).contains(RequestParameterProperty.REQUEST_PARAMETER_CSRF_TOKEN, RequestParameterProperty.REQUEST_PARAMETER_IGNORE);
    }

    private void mockFormAndElement(String methodName, String tokenInputName, String tokenInputValue, String tokenInputType) throws Exception {
        Form formMock = mock(Form.class);
        when(formMock.getMethod()).thenReturn(methodName);
        formField = new FormField(tokenInputName, tokenInputValue);
        when(formMock.getFormField(tokenInputName)).thenReturn(Collections.singletonList(formField));
        when(formResponseProcessor.getForm()).thenReturn(formMock);
        Element element = mock(Element.class);
        when(element.attr("value")).thenReturn(tokenInputValue);
        when(element.attr("type")).thenReturn(tokenInputType);
        when(formMock.selectOne(elementSelectorWithAttribute("input", "name", tokenInputName))).thenReturn(element);
    }
}