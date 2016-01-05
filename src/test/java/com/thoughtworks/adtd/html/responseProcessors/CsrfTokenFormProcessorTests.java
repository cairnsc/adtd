package com.thoughtworks.adtd.html.responseProcessors;

import com.thoughtworks.adtd.html.ElementAttributeException;
import com.thoughtworks.adtd.html.ElementTypeException;
import com.thoughtworks.adtd.html.Form;
import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.Response;
import org.jsoup.nodes.Element;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.thoughtworks.adtd.util.SelectorStringBuilder.elementSelectorWithAttribute;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CsrfTokenFormProcessorTests {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private final FormResponseProcessor formResponseProcessor = mock(FormResponseProcessor.class);

    @Test
    public void shouldThrowExceptionIfTokenWillBeExposedInRequest() throws Exception {
        String tokenInputName = "token";
        mockFormAndElement("GET", tokenInputName, "abc123", "hidden");
        CsrfTokenFormProcessor processor = new CsrfTokenFormProcessor(formResponseProcessor, tokenInputName);
        expectedException.expect(ElementTypeException.class);
        expectedException.expectMessage("Element input with name=\"" + tokenInputName + "\" has method=\"GET\", expected \"POST\"");

        processor.process(mock(Request.class), mock(Response.class));
    }

    @Test
    public void shouldThrowExceptionIfTokenInputValueIsNull() throws Exception {
        String tokenInputName = "token";
        mockFormAndElement("POST", tokenInputName, null, "hidden");
        CsrfTokenFormProcessor processor = new CsrfTokenFormProcessor(formResponseProcessor, tokenInputName);
        expectedException.expect(ElementAttributeException.class);
        expectedException.expectMessage("Element input with name=\"" + tokenInputName + "\" has no value");

        processor.process(mock(Request.class), mock(Response.class));
    }

    @Test
    public void shouldThrowExceptionIfTokenInputValueIsEmpty() throws Exception {
        String tokenInputName = "token";
        mockFormAndElement("POST", tokenInputName, " \t", "hidden");
        CsrfTokenFormProcessor processor = new CsrfTokenFormProcessor(formResponseProcessor, tokenInputName);
        expectedException.expect(ElementAttributeException.class);
        expectedException.expectMessage("Element input with name=\"" + tokenInputName + "\" has no value");

        processor.process(mock(Request.class), mock(Response.class));
    }

    @Test
    public void shouldThrowExceptionIfInputTokenIsNotHidden() throws Exception {
        String tokenInputName = "token";
        mockFormAndElement("POST", tokenInputName, "abc123", "text");
        CsrfTokenFormProcessor processor = new CsrfTokenFormProcessor(formResponseProcessor, tokenInputName);
        expectedException.expect(ElementTypeException.class);
        expectedException.expectMessage("Element input with name=\"" + tokenInputName + "\" has type=\"text\", expected \"hidden\"");

        processor.process(mock(Request.class), mock(Response.class));

    }

    private void mockFormAndElement(String methodName, String tokenInputName, String tokenInputValue, String tokenInputType) throws Exception {
        Form form = mock(Form.class);
        when(form.getMethod()).thenReturn(methodName);
        when(formResponseProcessor.getForm()).thenReturn(form);
        Element element = mock(Element.class);
        when(element.attr("value")).thenReturn(tokenInputValue);
        when(element.attr("type")).thenReturn(tokenInputType);
        when(form.selectOne(elementSelectorWithAttribute("input", "name", tokenInputName))).thenReturn(element);
    }
}