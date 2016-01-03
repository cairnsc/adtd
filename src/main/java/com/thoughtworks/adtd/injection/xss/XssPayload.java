package com.thoughtworks.adtd.injection.xss;

import java.util.regex.Pattern;

/**
 * Contains an XSS payload.
 */
public class XssPayload {
    /**
     * Simple payloads to test. In the near future this will be expanded upon.
     */
    public static final String[] PAYLOAD_LIST = {
        "<script>adtd();</script>",
        "<script src=\"adtd\"/>",
        "<body onload=alert(1)>",
        "<body/onhashchange=alert(1)><a href=#>click",
        "<img src=javascript:alert(1)>"
    };

    private final String payload;
    private final Pattern compiledPattern;

    public XssPayload(String payload) {
        this.payload = payload;
        compiledPattern = Pattern.compile(Pattern.quote(payload), Pattern.CASE_INSENSITIVE);
    }

    /**
     * Get the XSS payload.
     * @return XSS payload.
     */
    public String getPayload() {
        return payload;
    }

    /**
     * Test if content contains this XSS payload.
     * @param content Content to find the XSS payload in.
     * @return Boolean indicating whether the content contains this XSS payload.
     */
    public boolean matches(String content) {
        return compiledPattern.matcher(content).find();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XssPayload that = (XssPayload) o;

        return payload.equals(that.payload);

    }

    @Override
    public int hashCode() {
        return payload.hashCode();
    }
}
