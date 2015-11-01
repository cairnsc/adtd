package com.thoughtworks.adtd.html.responseProcessors;

import com.thoughtworks.adtd.html.Form;
import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.Response;
import com.thoughtworks.adtd.testutil.BasicHtmlForm;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FormResponseProcessorTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldThrowExceptionInGetFormIfResponseNotReceived() throws Exception {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A response has not yet been processed");
        FormResponseProcessor processor = new FormResponseProcessor(mock(HtmlResponseProcessor.class), BasicHtmlForm.FORM_ACTION);

        processor.getForm();
    }

    @Test
    public void shouldProcessForm() throws Exception {
        HtmlResponseProcessor htmlResponseProcessor = mock(HtmlResponseProcessor.class);
        Document document = Jsoup.parse(BasicHtmlForm.HTML);
        when(htmlResponseProcessor.getDocument()).thenReturn(document);
        FormResponseProcessor processor = new FormResponseProcessor(htmlResponseProcessor, BasicHtmlForm.FORM_ACTION);

        processor.process(mock(Request.class), mock(Response.class));

        Form form = processor.getForm();
        assertThat(form.getAction()).isEqualTo(BasicHtmlForm.FORM_ACTION);
        assertThat(form.getMethod()).isEqualToIgnoringCase(BasicHtmlForm.FORM_METHOD);
    }
}