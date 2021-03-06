package com.thoughtworks.adtd.http.responseConditions.status;

import com.thoughtworks.adtd.http.HttpStatus;
import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.Response;
import com.thoughtworks.adtd.http.ResponseCondition;
import com.thoughtworks.adtd.util.AssertionFailureException;
import com.thoughtworks.adtd.util.failureMessages.ShouldHaveValue;

/**
 * Response condition to evaluate whether a response has a specified status code.
 */
public class HasStatusCode implements ResponseCondition {
    private final int statusCode;

    public HasStatusCode(HttpStatus statusCode) {
        this(statusCode.getStatusCode());
    }

    public HasStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void match(Request request, Response response) throws Exception {
        if (response.getStatus() != statusCode) {
            throw new AssertionFailureException(ShouldHaveValue.shouldHaveValue(
                    "HTTP response status code", statusCode, response.getStatus(), request.getContext()
            ));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HasStatusCode that = (HasStatusCode) o;

        return statusCode == that.statusCode;

    }

    @Override
    public int hashCode() {
        return statusCode;
    }
}