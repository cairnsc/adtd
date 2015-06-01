package com.thoughtworks.adtd.http;

public interface RequestExecutor {

    Response execute(WebProxy proxy) throws Exception;

}
