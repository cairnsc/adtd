package com.thoughtworks.adtd.html;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public interface Form {
    Elements select(String cssQuery);
    Elements selectExact(String cssQuery, int numElements) throws Exception;
    Element selectOne(String cssQuery) throws Exception;
    String getMethod() throws Exception;
}
