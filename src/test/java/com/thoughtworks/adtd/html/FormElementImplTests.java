package com.thoughtworks.adtd.html;

import com.thoughtworks.adtd.http.ElementAttributeException;
import com.thoughtworks.adtd.http.ElementCountException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.parser.Tag;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.thoughtworks.adtd.util.SelectorStringBuilder.elementSelector;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FormElementImplTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldThrowExceptionIfFormElementNotFound() throws Exception {
        String formAction = "test";
        String html = "<html><body></body></html>";
        Document doc = Jsoup.parse(html);
        expectedException.expect(ElementCountException.class);
        expectedException.expectMessage("Expected 1 form elements with action=\"" + formAction + "\", found 0");

        FormElementImpl.getFormFromDocument(doc, formAction);
    }

    @Test
    public void shouldThrowExceptionIfMultipleFormElementsFound() throws Exception {
        String formAction = "test";
        String formHtml = "<form action=\"" + formAction + "\"></form>";
        String html = "<html><body>" + formHtml + formHtml + "</body></html>";
        Document doc = Jsoup.parse(html);
        expectedException.expect(ElementCountException.class);
        expectedException.expectMessage("Expected 1 form elements with action=\"" + formAction + "\", found 2");

        FormElementImpl.getFormFromDocument(doc, formAction);
    }

    @Test
    public void shouldSelectInForm() {
        FormElement formElement = mock(FormElement.class);
        FormElementImpl form = new FormElementImpl(formElement);

        String cssQuery = "input";
        form.select(cssQuery);

        verify(formElement).select(cssQuery);
    }

    @Test
    public void shouldThrowExceptionIfSelectExactElementCountDoesNotMatch() throws Exception {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "/", new Attributes());
        String elementName = "input";
        formElement.addElement(new Element(Tag.valueOf(elementName), "/"));
        formElement.addElement(new Element(Tag.valueOf(elementName), "/"));
        FormElementImpl form = new FormElementImpl(formElement);
        expectedException.expect(ElementCountException.class);
        expectedException.expectMessage("Expected 1 element matching \"" + elementSelector(elementName) + "\", found 2");

        form.selectExact(elementSelector(elementName), 1);
    }

    @Test
    public void shouldGetMethod() throws Exception {
        String method = "GET";
        Attributes attributes = new Attributes();
        attributes.put("method", method);
        FormElement formElement = new FormElement(Tag.valueOf("form"), "/", attributes);
        FormElementImpl form = new FormElementImpl(formElement);

        String formMethod = form.getMethod();

        assertThat(formMethod).isEqualTo(method);
    }

    @Test
    public void shouldMakeMethodUppercase() throws Exception {
        Attributes attributes = new Attributes();
        attributes.put("method", "get");
        FormElement formElement = new FormElement(Tag.valueOf("form"), "/", attributes);
        FormElementImpl form = new FormElementImpl(formElement);

        String formMethod = form.getMethod();

        assertThat(formMethod).isEqualTo("GET");
    }

    @Test
    public void shouldStripMethodWhitespace() throws Exception {
        Attributes attributes = new Attributes();
        attributes.put("method", "  post ");
        FormElement formElement = new FormElement(Tag.valueOf("form"), "/", attributes);
        FormElementImpl form = new FormElementImpl(formElement);

        String formMethod = form.getMethod();

        assertThat(formMethod).isEqualTo("POST");
    }

    @Test
    public void shouldThrowExceptionIfMethodIsNotGetOrPost() throws Exception {
        Attributes attributes = new Attributes();
        attributes.put("method", "put");
        FormElement formElement = new FormElement(Tag.valueOf("form"), "/", attributes);
        FormElementImpl form = new FormElementImpl(formElement);
        expectedException.expect(ElementAttributeException.class);
        expectedException.expectMessage("Element form with method=\"put\" is not a valid method");

        form.getMethod();
    }

}
