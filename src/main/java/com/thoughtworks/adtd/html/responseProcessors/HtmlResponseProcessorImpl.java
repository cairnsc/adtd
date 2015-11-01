package com.thoughtworks.adtd.html.responseProcessors;

import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HtmlResponseProcessorImpl implements HtmlResponseProcessor {
    private Response response;
    private Document document;

    public Document getDocument() {
        checkResponseProcessed();
        return document;
    }

    public void prepare(Request request) {
    }

    public void process(Request request, Response response) throws Exception {
        this.response = response;
        // REVISIT: verify content type?
        String body = response.getBody();
        document = Jsoup.parse(body);
    }

    private void checkMutability() {
        if (response != null) {
            throw new IllegalStateException("A response has already been processed");
        }
    }

    private void checkResponseProcessed() {
        if (response == null) {
            throw new IllegalStateException("A response has not yet been processed");
        }
    }
}
