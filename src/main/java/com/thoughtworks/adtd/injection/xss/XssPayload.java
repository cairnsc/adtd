package com.thoughtworks.adtd.injection.xss;

import java.util.regex.Pattern;

public class XssPayload {
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

    public String getPayload() {
        return payload;
    }

    public boolean matches(String content) {
        return compiledPattern.matcher(content).find();
    }
}
