package com.thoughtworks.adtd.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.FormElement;
import org.jsoup.parser.Tag;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static com.thoughtworks.adtd.util.SelectorStringBuilder.elementSelector;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FormTests {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldThrowExceptionIfFormElementNotFound() throws Exception {
        String formAction = "test";
        String html = "<html><body></body></html>";
        Document doc = Jsoup.parse(html);
        expectedException.expect(ElementCountException.class);
        expectedException.expectMessage("Expected 1 form elements with action=\"" + formAction + "\", found 0");

        Form.getFormFromDocument(doc, formAction);
    }

    @Test
    public void shouldThrowExceptionIfMultipleFormElementsFound() throws Exception {
        String formAction = "test";
        String formHtml = "<form action=\"" + formAction + "\"></form>";
        String html = "<html><body>" + formHtml + formHtml + "</body></html>";
        Document doc = Jsoup.parse(html);
        expectedException.expect(ElementCountException.class);
        expectedException.expectMessage("Expected 1 form elements with action=\"" + formAction + "\", found 2");

        Form.getFormFromDocument(doc, formAction);
    }

    @Test
    public void shouldSelectInForm() {
        FormElement formElement = mock(FormElement.class);
        Form form = new Form(formElement);

        String cssQuery = "input";
        form.select(cssQuery);

        verify(formElement).select(cssQuery);
    }

    @Test
    public void shouldThrowExceptionIfSelectExactElementCountDoesNotMatch() throws Exception {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "/", new Attributes());
        String elementName = "input";
        formElement.append("<input></input>");
        formElement.append("<input></input>");
        Form form = new Form(formElement);
        expectedException.expect(ElementCountException.class);
        expectedException.expectMessage("Expected 1 elements matching \"" + elementSelector(elementName) + "\", found 2");

        form.selectExact(elementSelector(elementName), 1);
    }

    @Test
    public void shouldGetMethod() throws Exception {
        String method = "GET";
        Attributes attributes = new Attributes();
        attributes.put("method", method);
        FormElement formElement = new FormElement(Tag.valueOf("form"), "/", attributes);
        Form form = new Form(formElement);

        String formMethod = form.getMethod();

        assertThat(formMethod).isEqualTo(method);
    }

    @Test
    public void shouldMakeMethodUppercase() throws Exception {
        FormElement formElement = createBasicFormElement("get");
        Form form = new Form(formElement);

        String formMethod = form.getMethod();

        assertThat(formMethod).isEqualTo("GET");
    }

    @Test
    public void shouldStripMethodWhitespace() throws Exception {
        FormElement formElement = createBasicFormElement("  post ");
        Form form = new Form(formElement);

        String formMethod = form.getMethod();

        assertThat(formMethod).isEqualTo("POST");
    }

    @Test
    public void shouldThrowExceptionIfMethodIsNotGetOrPost() throws Exception {
        Attributes attributes = new Attributes();
        attributes.put("method", "put");
        FormElement formElement = new FormElement(Tag.valueOf("form"), "/", attributes);
        Form form = new Form(formElement);
        expectedException.expect(ElementAttributeException.class);
        expectedException.expectMessage("Element form with method=\"put\" is not a valid method");

        form.getMethod();
    }

    @Test
    public void shouldGetAction() throws Exception {
        String action = "/action";
        Attributes attributes = new Attributes();
        attributes.put("action", action);
        FormElement formElement = new FormElement(Tag.valueOf("form"), "/", attributes);
        Form form = new Form(formElement);

        String formAction = form.getAction();

        assertThat(formAction).isEqualTo(action);
    }

    @Test
    public void shouldThrowExceptionIfActionIsNotSpecified() throws Exception {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "/", new Attributes());
        Form form = new Form(formElement);
        expectedException.expect(ElementAttributeRequiredException.class);
        expectedException.expectMessage("Element form is missing required attribute \"action\"");

        form.getAction();
    }

    @Test
    public void shouldGetFormInputsFromFormElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "/", new Attributes());
        formElement.append("<input name=\"a\" value=\"b\"></input>");
        formElement.append("<input name=\"c\" value=\"d\"></input>");
        Form form = new Form(formElement);

        List<FormField> formInputs = form.getFormFields();

        assertThat(formInputs.size()).isEqualTo(2);
        assertThat(formInputs.size()).isEqualTo(form.countFormFields());
        assertThat(formInputs.get(0).getName()).isEqualTo("a");
        assertThat(formInputs.get(0).getValue()).isEqualTo("b");
        assertThat(formInputs.get(1).getName()).isEqualTo("c");
        assertThat(formInputs.get(1).getValue()).isEqualTo("d");
    }

    private FormElement createBasicFormElement(String method) {
        Attributes attributes = new Attributes();
        attributes.put("method", method);
        return new FormElement(Tag.valueOf("form"), "/", attributes);
    }
}
