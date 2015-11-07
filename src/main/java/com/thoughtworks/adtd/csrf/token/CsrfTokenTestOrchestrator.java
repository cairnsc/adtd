package com.thoughtworks.adtd.csrf.token;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.thoughtworks.adtd.csrf.token.strategies.TestStrategy;
import com.thoughtworks.adtd.csrf.token.strategies.TestStrategyEmptyToken;
import com.thoughtworks.adtd.csrf.token.strategies.TestStrategyInvalidToken;
import com.thoughtworks.adtd.csrf.token.strategies.TestStrategyPositive;
import com.thoughtworks.adtd.html.Form;
import com.thoughtworks.adtd.html.FormData;
import com.thoughtworks.adtd.http.ResponseValidator;

import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Tests:
 *  1. Positive case: valid token submitted
 *  2. Negative case: missing token
 *  3. Negative case: invalid token
 */
public class CsrfTokenTestOrchestrator implements Iterator<CsrfTokenTest> {
    public static Iterable<Object[]> asIterableOfArrays(final Form form, final String tokenInputName, final ResponseValidator validator) {
        return FluentIterable
                .from(new Iterable<CsrfTokenTest>() {
                    public Iterator<CsrfTokenTest> iterator() {
                        return new CsrfTokenTestOrchestrator(form, tokenInputName, validator);
                    }
                })
                .transform(new Function<CsrfTokenTest, Object[]>() {
                    public Object[] apply(CsrfTokenTest input) {
                        return new Object[]{ input };
                    }
                });
    }

    private final String tokenInputName;
    private final ResponseValidator validator;
    private List<CsrfTokenTest> testList;
    private final Iterator<CsrfTokenTest> iterator;

    /**
     * @param form
     * @param tokenInputName
     * @param validator
     */
    public CsrfTokenTestOrchestrator(Form form, String tokenInputName, ResponseValidator validator) {
        this.tokenInputName = tokenInputName;
        this.validator = validator;
        testList = newArrayList();
        addTest(form, validator, new TestStrategyPositive(tokenInputName));
        addTest(form, validator, new TestStrategyEmptyToken(tokenInputName));
        addTest(form, validator, new TestStrategyInvalidToken(tokenInputName));
        iterator = testList.iterator();
    }

    private void addTest(Form form, ResponseValidator validator, TestStrategy strategy) {
        FormData formData = new FormData(form);
        testList.add(new CsrfTokenTest(strategy, form, formData, validator));
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
