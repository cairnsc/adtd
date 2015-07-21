package com.thoughtworks.adtd.csrf.token;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.thoughtworks.adtd.http.ResponseValidator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Tests:
 *  1. Positive case: valid token submitted
 *  2. Negative case: missing token
 *  3. Negative case: invalid token
 *
 * TODO: iterator with strategies (single request or request per test)
 */
public class CsrfTokenTestIteratorImpl implements CsrfTokenTestIterator {

    public static Iterable<Object[]> asIterableOfArrays(final String formAction, final String tokenInputName, final ResponseValidator validator) {
        return FluentIterable
                .from(new Iterable<CsrfTokenTest>() {
                    public Iterator<CsrfTokenTest> iterator() {
                        return new CsrfTokenTestIteratorImpl(formAction, tokenInputName, validator);
                    }
                })
                .transform(new Function<CsrfTokenTest, Object[]>() {
                    public Object[] apply(CsrfTokenTest input) {
                        return new Object[]{ input };
                    }
                });
    }


    private final String formAction;
    private final String tokenInputName;
    private final ResponseValidator validator;
    private List<CsrfTokenTest> testList;
    private final Iterator<CsrfTokenTest> iterator;

    /**
     * @param formAction Form action attribute.
     * @param tokenInputName Name of the input tag containing the CSRF token.
     * @param validator Response validator.
     */
    public CsrfTokenTestIteratorImpl(String formAction, String tokenInputName, ResponseValidator validator) {
        this.formAction = formAction;
        this.tokenInputName = tokenInputName;
        this.validator = validator;
        // list may not be the best way to do this long term
        testList = new ArrayList<CsrfTokenTest>();
        testList.add(new CsrfTokenTestImpl(formAction, tokenInputName, validator));
        iterator = testList.iterator();
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public CsrfTokenTest next() {
        return iterator.next();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public int count() {
        return testList.size();
    }

}
