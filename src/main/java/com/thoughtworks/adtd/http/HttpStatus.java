package com.thoughtworks.adtd.http;

/**
 * Enumeration of HTTP status codes and their respective reason phrases.
 */
public enum HttpStatus {
    OK(200, "OK");

    private final int statusCode;
    private final String reasonPhrase;

    HttpStatus(int statusCode, String reasonPhrase) {
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public boolean equals(int statusCode) {
        return (statusCode == this.statusCode);
    }

    @Override
    public String toString() {
        return Integer.toString(statusCode);
    }

}