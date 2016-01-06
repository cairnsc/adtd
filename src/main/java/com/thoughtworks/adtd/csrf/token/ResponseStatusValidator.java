package com.thoughtworks.adtd.csrf.token;

import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.Response;

/**
 * Response validator to determine whether a {@link CsrfTokenTest} passed based on the response status code.
 */
public class ResponseStatusValidator implements ResponseValidator {
    private final int statusPositive;
    private final int statusNegative;

    /**
     * @param statusPositive Status code expected for a positive test (i.e. with a valid CSRF token).
     * @param statusNegative Status code expected for a negative test (i.e. with an invalid CSRF token).
     */
    public ResponseStatusValidator(int statusPositive, int statusNegative) {
        this.statusPositive = statusPositive;
        this.statusNegative = statusNegative;
    }

    public boolean validate(CsrfTokenTest test, Request request, Response response) {
        if (test.isPositiveTest()) {
            return (response.getStatus() == statusPositive);
        } else {
            return (response.getStatus() == statusNegative);
        }
    }
}
