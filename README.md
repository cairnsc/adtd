# adtd
Attack-Driven Test Development

This library is an experiment to approach software testing for the verification of security threat mitigations in code
during development. It could be used as part of anything from unit tests through end-to-end tests.

The objective of the library is to provide developers with a way to verify mitigations - whether applied from a popular 
framework or as custom code - adequately address threats. The focus will initially be on common classes of
vulnerabilities, beginning with those found in the OWASP Top 10.

## Reflected XSS
Test the outcome of rendering a page where user-supplied data from the request is included within the output.

## Persisted XSS
Test the outcome of rendering a page where user-supplied data is loaded from a data store and included within the output.

Possible strategy:
* Mock data source and feed it with data from the generator.
