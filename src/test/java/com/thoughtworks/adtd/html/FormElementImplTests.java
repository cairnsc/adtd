package com.thoughtworks.adtd.html;

import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.RequestExecutor;
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
        formElement.append("<input></input>");
        formElement.append("<input></input>");
        FormElementImpl form = new FormElementImpl(formElement);
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

    @Test
    public void shouldGetAction() throws Exception {
        String action = "/action";
        Attributes attributes = new Attributes();
        attributes.put("action", action);
        FormElement formElement = new FormElement(Tag.valueOf("form"), "/", attributes);
        FormElementImpl form = new FormElementImpl(formElement);

        String formAction = form.getAction();

        assertThat(formAction).isEqualTo(action);
    }

    @Test
    public void shouldThrowExceptionIfActionIsNotSpecified() throws Exception {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "/", new Attributes());
        FormElementImpl form = new FormElementImpl(formElement);
        expectedException.expect(ElementAttributeRequiredException.class);
        expectedException.expectMessage("Element form is missing required attribute \"action\"");

        form.getAction();
    }

    @Test
    public void shouldGetFormDataFromFormElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "/", new Attributes());
        formElement.append("<input name=\"a\" value=\"b\"></input>");
        formElement.append("<input name=\"c\" value=\"d\"></input>");
        FormElementImpl form = new FormElementImpl(formElement);

        List<FormFieldData> formInputs = form.getFormFields();

        assertThat(formInputs.size()).isEqualTo(2);
        assertThat(formInputs.size()).isEqualTo(form.countFormFields());
        FormFieldData formFieldData = formInputs.get(0);
        assertThat(formFieldData.getName()).isEqualTo("a");
        assertThat(formFieldData.getValue()).isEqualTo("b");
        formFieldData = formInputs.get(1);
        assertThat(formFieldData.getName()).isEqualTo("c");
        assertThat(formFieldData.getValue()).isEqualTo("d");
    }

    @Test
    public void shouldCreateRequest() throws Exception {
        String method = "POST";
        String action = "/action";
        Attributes attributes = new Attributes();
        attributes.put("method", method);
        attributes.put("action", action);
        FormElement formElement = new FormElement(Tag.valueOf("form"), "/", attributes);
        FormElementImpl form = new FormElementImpl(formElement);
        RequestExecutor requestExecutor = mock(RequestExecutor.class);

        Request request = form.createRequest(requestExecutor);

        assertThat(request.getMethod()).isEqualTo(method);
        assertThat(request.getUri()).isEqualTo(action);
    }
}
