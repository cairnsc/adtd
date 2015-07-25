package com.thoughtworks.adtd.http;

public interface Response {

    int getStatus();
    String getHeader(String name);
    String getBody() throws Exception;

}
