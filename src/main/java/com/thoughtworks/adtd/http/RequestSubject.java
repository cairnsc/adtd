package com.thoughtworks.adtd.http;

public interface RequestSubject {

    Response execute(WebProxy proxy) throws Exception;

}
