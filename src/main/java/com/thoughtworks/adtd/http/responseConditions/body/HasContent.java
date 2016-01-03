package com.thoughtworks.adtd.http.responseConditions.body;

import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.Response;
import com.thoughtworks.adtd.http.ResponseCondition;
import com.thoughtworks.adtd.util.AssertionFailureException;
import com.thoughtworks.adtd.util.failureMessages.ShouldBeEmpty;
import com.thoughtworks.adtd.util.failureMessages.ShouldNotBeEmpty;

public class HasContent implements ResponseCondition {
    private final boolean shouldHaveContent;

    public HasContent() {
        this(true);
    }

    public HasContent(boolean shouldHaveContent) {
        this.shouldHaveContent = shouldHaveContent;
    }

    public void match(Request request, Response response) throws Exception {
        String body = response.getBody();

        if (shouldHaveContent) {
            if (body == null || body.isEmpty()) {
                throw new AssertionFailureException(ShouldNotBeEmpty.shouldNotBeEmpty(
                        "HTTP response body", request.getContext())
                );
            }
        } else {
            if (body != null && !body.isEmpty()) {
                throw new AssertionFailureException(ShouldBeEmpty.shouldBeEmpty(
                        "HTTP response body", request.getContext())
                );
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HasContent that = (HasContent) o;

        return shouldHaveContent == that.shouldHaveContent;

    }

    @Override
    public int hashCode() {
        return (shouldHaveContent ? 1 : 0);
    }
}
