package com.thoughtworks.adtd.xss;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

import java.util.Iterator;

public class XssTestIteratorImpl implements XssTestIterator {

    public static Iterable<Object[]> asIterableOfArrays() {
        return FluentIterable
                .from(new Iterable<XssTest>() {
                    public Iterator<XssTest> iterator() {
                        return new XssTestIteratorImpl();
                    }
                })
                .transform(new Function<XssTest, Object[]>() {
                    public Object[] apply(XssTest input) {
                        return new Object[]{ input };
                    }
                });
    }

    private static final String[] ATTACK_LIST = {
        "<script>adtd();</script>",
        "<script src=\"adtd\"/>",
        "<body onload=alert(1)>",
        "<body/onhashchange=alert(1)><a href=#>click",
        "<img src=javascript:alert(1)>"
    };

    private int currentIdx;

    public XssTestIteratorImpl() {
        currentIdx = 0;
    }

    public boolean hasNext() {
        return (currentIdx < ATTACK_LIST.length);
    }

    public XssTest next() {
        XssTestImpl xssTest = new XssTestImpl(ATTACK_LIST[currentIdx]);
        currentIdx++;
        return xssTest;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public int count() {
        return ATTACK_LIST.length;
    }

}
