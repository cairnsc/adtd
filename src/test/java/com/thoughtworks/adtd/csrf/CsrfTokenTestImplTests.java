package com.thoughtworks.adtd.csrf;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CsrfTokenTestImplTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private CsrfTokenTestImpl test;

    @Test
    public void shouldThrowExceptionIfPrepareRetrieveInvokedMoreThanOnce() {
        test = new CsrfTokenTestImpl();
        test.prepareRetrieve("test", "/test");
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A retrieve request has already been prepared for this test");

        test.prepareRetrieve("test", "/test");
    }

    @Test
    public void shouldGetFormDataAftterPrepareCompletes() {

    }

    @Test
    public void shouldThrowExceptionIfFormDataNotAvailable() throws Exception {
        test = new CsrfTokenTestImpl();
        expectedException.expectMessage("A retrieve request must first be executed for this test");

        test.getFormData();
    }

    @Test
    public void shouldThrowExceptionIfPrepareSubmitInvokedBeforeRetrieveRequestCreated() throws Exception {
        test = new CsrfTokenTestImpl();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A retrieve request must first be executed for this test");

        test.prepareSubmit();
    }

    @Test
    public void shouldThrowExceptionIfPrepareSubmitInvokedBeforeRetrieveRequestExecuted() throws Exception {
        test = new CsrfTokenTestImpl();
        test.prepareRetrieve("test", "/test");
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A retrieve request must first be executed for this test");

        test.prepareSubmit();
    }

    @Test
    public void shouldThrowExceptionIfPrepareSubmitInvokedMoreThanOnce() throws Exception {
        test = new CsrfTokenTestImpl();
        test.prepareRetrieve("test", "/test");
        test.notifyRequestComplete();
        test.prepareSubmit();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("A submit request has already been prepared for this test");

        test.prepareSubmit();
    }

}
