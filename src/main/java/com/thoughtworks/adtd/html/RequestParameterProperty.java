package com.thoughtworks.adtd.html;

public enum RequestParameterProperty {
    /**
     * Specify the request parameter should be ignored in tests, except when used in conjunction with another flag that
     * specifies a specific context to use it in.
     */
    REQUEST_PARAMETER_IGNORE,

    /**
     * Specify the request parameter contains a CSRF token.
     */
    REQUEST_PARAMETER_CSRF_TOKEN
}
