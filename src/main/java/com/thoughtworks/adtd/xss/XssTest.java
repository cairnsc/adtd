package com.thoughtworks.adtd.xss;

public interface XssTest {
    String getTestPattern();
    boolean matches(String content);
}
