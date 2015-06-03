package com.thoughtworks.adtd.http.responseConditions.status;

import com.thoughtworks.adtd.http.HttpStatus;
import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.Response;
import com.thoughtworks.adtd.http.ResponseCondition;
import com.thoughtworks.adtd.util.AssertionFailureException;

/**
 * Test whether a response has a particular status code.
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
            throw new AssertionFailureException("HTTP response status code", statusCode, response.getStatus());
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
