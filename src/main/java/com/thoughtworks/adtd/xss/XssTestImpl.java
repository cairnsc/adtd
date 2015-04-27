package com.thoughtworks.adtd.xss;

import java.util.regex.Pattern;

public class XssTestImpl implements XssTest {

    private String testPattern;

    public XssTestImpl(String testPattern) {
        this.testPattern = testPattern;
    }

    public String getTestPattern() {
        return testPattern;
    }

    public boolean matches(String content) {
        return Pattern.compile(Pattern.quote(testPattern), Pattern.CASE_INSENSITIVE).matcher(content).find();
    }

}
