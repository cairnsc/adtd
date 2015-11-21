# adtd
adtd is a framework for Attack-Driven Test Development.

The framework strives to simplify the task of verifying threat mitigations in code during development. The tests it includes today are intended for use in high-level tests such as integration tests.

## Goals
The goal of adtd is to provide a straightforward way of verifying security threat mitigations are operating correctly. This includes mitigations against common problems such as those found in the OWASP Top 10 along with other important security controls.

Creation of the framework was motivated by the lack of a simplified means of testing these concerns on projects that make heavy use of automated testing.

## Getting started
In order to use adtd, you must create a class that implements the WebProxy interface to execute requests generated by tests, and another class that implements the Response interface so the tests can process the result of the request.

## Structure
Suites of tests are categorized by the attack type (for example, Cross-Site Scripting) and bundled into individual packages. Each test suite contains an test orchestrator, whose responsibility is to execute tests.

## Preparing tests
In order to execute tests, information about the resource to test is needed. A class to prepare this manually will be introduced soon.

### Forms and FormData


### Reconnaissance
The FormRetrieveRequest class is provided to to request a resource containing a form and produce a Form object that can be used within tests. This method of preparing a Form is preferable since it reduces the risk of forgetting to test a parameter as the resource it is testing evolves over time.

## Cross-Site Scripting (XSS) Tests
XSS tests are found in com.thoughtworks.adtd.injection.xss and orchestrated using XssTestOrchestrator.

### Testing for Reflected XSS
When data received in a request will be reflected back to a user in a response, you should test for reflected XSS. This is done by providing an instance of TestStrategyIteratorForm to XssTestOrchestrator.

### Testing for Persistent XSS
If data received in a request is persisted to a data store and later sent in a separate response, you should test for persistent XSS. This is done by providing an instance of TestStrategyIteratorBasic to XssTestOrchestrator. The test makes the assumption the underlying data store will be mocked and that the test pattern can be provided by the mock.

## Cross-Site Request Forgery (CSRF) Tests
If Cross-Site Request Forgery mitigations are in place, you should verify the mitigations are functioning correctly. CSRF tests are found in com.thoughtworks.adtd.csrf.token and orchestrated using CsrfTokenTestOrchestrator.

## HTTP Response Splitting Tests
If data received in a request is reflected back to a user in a response, you should also test for HTTP Response Splitting. This can be done using HttpResponseSplittingTestOrchestrator in com.thoughtworks.adtd.injection.http, which tests for reflected HTTP Response Splitting. Support for the persistent case will be added soon.
