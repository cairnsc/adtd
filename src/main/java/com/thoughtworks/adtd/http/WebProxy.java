package com.thoughtworks.adtd.http;

public interface WebProxy {

    Response execute(Request request) throws Exception;

}
