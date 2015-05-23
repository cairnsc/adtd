package com.thoughtworks.adtd.injection.xss;

import java.util.Iterator;

public interface XssTestIterator extends Iterator<XssTest> {
    int count();
}
