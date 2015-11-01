package com.thoughtworks.adtd.html.responseProcessors;

import com.thoughtworks.adtd.http.ResponseProcessor;
import org.jsoup.nodes.Document;

public interface HtmlResponseProcessor extends ResponseProcessor {
    /**
     * Get the parsed HTML Document.
     * @return Document.
     */
    Document getDocument() throws Exception;
}
