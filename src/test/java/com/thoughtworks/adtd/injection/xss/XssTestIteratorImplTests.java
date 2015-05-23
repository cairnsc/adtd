package com.thoughtworks.adtd.injection.xss;

import org.junit.Test;

public class XssTestIteratorImplTests {

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowExceptionOnRemove() {
        XssTestIteratorImpl iterator = new XssTestIteratorImpl();

        iterator.remove();
    }

}
