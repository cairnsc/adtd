package com.thoughtworks.adtd.http;

/**
 * Represents a HTTP header.
 */
public interface Header {
    /**
     * Get the header name.
     * @return Header name.
     */
    String getName();

    /**
     * Compare the header name with a given name.
     * @param name Name to compare header name with.
     * @return Boolean indicating whether the name matches the header name.
     */
    boolean nameEquals(String name);

    /**
     * Get the header value.
     * @return Header value.
     */
    String getValue();
}
