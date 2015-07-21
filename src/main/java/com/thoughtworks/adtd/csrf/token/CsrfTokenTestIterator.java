package com.thoughtworks.adtd.csrf.token;

import java.util.Iterator;

public interface CsrfTokenTestIterator extends Iterator<CsrfTokenTest> {
    int count();
}
