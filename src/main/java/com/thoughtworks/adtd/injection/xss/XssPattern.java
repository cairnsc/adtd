package com.thoughtworks.adtd.injection.xss;

import java.util.regex.Pattern;

public class XssPattern {
    private final String testPattern;
    private final Pattern compiledPattern;

    public XssPattern(String testPattern) {
        this.testPattern = testPattern;
        compiledPattern = Pattern.compile(Pattern.quote(testPattern), Pattern.CASE_INSENSITIVE);
    }

    public String getInjectionString() {
        return testPattern;
    }

    public boolean matches(String content) {
        return compiledPattern.matcher(content).find();
    }
}
