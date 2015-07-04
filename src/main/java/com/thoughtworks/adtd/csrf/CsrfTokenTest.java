package com.thoughtworks.adtd.csrf;

import com.thoughtworks.adtd.http.Request;

/**
 * Test to validate CSRF protection using the synchronizer token pattern.
 *
 * Read about the synchronizer token pattern here: https://www.owasp.org/index.php/Cross-Site_Request_Forgery_%28CSRF%29_Prevention_Cheat_Sheet#General_Recommendation:_Synchronizer_Token_Pattern
 *
 * To execute CSRF tests:
 *  1. Prepare form retrieval query and execute.
 *  2. Set form inputs.
 *  3. Prepare submit query and execute.
 *  4. Assert response.
 */
public interface CsrfTokenTest {

    /**
     * Prepare a request to retrieve the page containing the form to be tested. By default the request method is GET.
     * @param formAction
     * @param tokenInputName
     * @return
     */
    Request prepareRetrieve(String formAction, String tokenInputName);

    void setFormData(String name, String value);
    Request prepareSubmit();
    void assertResponse() throws Exception;

}
