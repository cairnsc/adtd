package com.thoughtworks.adtd.xss;

import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.util.AssertionFailureException;

public interface XssTest {

    String getTestPattern();

    // simple tests
    boolean matches(String content);

    // proxy tests
    Request prepare();
    void assertResponse() throws AssertionFailureException;

}
