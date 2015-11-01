package com.thoughtworks.adtd.html.responseProcessors;

import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.Response;
import com.thoughtworks.adtd.testutil.BasicHtml;
import org.jsoup.nodes.Document;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HtmlResponseProcessorImplTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldThrowExceptionInGetDocumentIfResponseNotProcessed() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A response has not yet been processed");
        HtmlResponseProcessorImpl processor = new HtmlResponseProcessorImpl();

        processor.getDocument();
    }

    @Test
    public void shouldProcessResponseBody() throws Exception {
        HtmlResponseProcessorImpl processor = new HtmlResponseProcessorImpl();
        Response response = mock(Response.class);
        when(response.getBody()).thenReturn(BasicHtml.HTML);

        processor.process(mock(Request.class), response);

        verify(response).getBody();
        Document document = processor.getDocument();
        assertThat(document.body().text()).isEqualTo(BasicHtml.BODY);
    }
}

