package com.thoughtworks.adtd.html;

import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.RequestExecutor;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URISyntaxException;
import java.util.List;

/**
 * Encapsulates a HTML form.
 */
public interface Form {
    Elements select(String cssQuery);
    Elements selectExact(String cssQuery, int numElements) throws Exception;
    Element selectOne(String cssQuery) throws Exception;

    String getMethod() throws Exception;
    String getAction() throws Exception;
    List<FormFieldData> getFormFields();

    Request createRequest(RequestExecutor executor) throws Exception;
}
