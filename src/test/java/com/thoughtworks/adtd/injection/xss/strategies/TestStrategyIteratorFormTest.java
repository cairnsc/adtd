package com.thoughtworks.adtd.injection.xss.strategies;

import com.thoughtworks.adtd.injection.xss.XssPayload;
import com.thoughtworks.adtd.testutil.TestForm;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.NoSuchElementException;

import static com.thoughtworks.adtd.testutil.Assertions.assertThat;

public class TestStrategyIteratorFormTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private TestStrategyIteratorForm iterator;
    private TestForm form;

    @Before
    public void setUp() {
        form = new TestForm("GET");
        form.addFormField("a", "A");
        form.addFormField("B", "b");
        iterator = new TestStrategyIteratorForm(form);
    }

    @Test
    public void shouldCreateTestStrategy() {
        TestStrategyFormField next = (TestStrategyFormField)iterator.next();

        assertThat(next.getFormFieldIdx()).isEqualTo(0);
        assertThat(next.getForm()).isSameAs(form);
        assertThat(next.getFormData()).isDerivedFrom(form);
    }

    @Test
    public void shouldExhaustIterator() {
        int count = iterator.count();
        assertThat(count).isEqualTo(XssPayload.PAYLOAD_LIST.length * form.countFormFields());

        int idx;
        for (idx = 0; idx < count; idx++) {
            TestStrategy next = iterator.next();
            assertThat(next).isNotNull();
        }

        assertThat(idx).isEqualTo(count);
        assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    public void shouldThrowExceptionOnNextWhenIteratorExhausted() {
        TestStrategyIteratorBasic iterator = new TestStrategyIteratorBasic();
        while (iterator.hasNext()) {
            iterator.next();
        }
        expectedException.expect(NoSuchElementException.class);

        iterator.next();
    }

    @Test
    public void shouldThrowExceptionOnRemove() {
        expectedException.expect(UnsupportedOperationException.class);

        iterator.remove();
    }
}