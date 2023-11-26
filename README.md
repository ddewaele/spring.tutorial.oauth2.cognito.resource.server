## Introduction

Simple Spring Boot oAuth2 Resource Server to test the Cognito Resource Server integration.
Demonstrates how to configure security using OAuth2 Resource Server and JWT (JSON Web Token) validation. 
The application defines security filter chains for different endpoints and leverages Spring Security for access control.

## Prerequisites

Before running the application, make sure you have the following prerequisites installed:

- Java Development Kit (JDK)
- Maven
- Git (optional, for cloning the repository)

The application will start on port 8090, and you can access it at `http://localhost:8090/`.

## Configuration

The security configuration for this application is defined in the `SecurityFilterChain` bean within the code. Here's a brief overview of the configuration:

- `/public`: Allows public access to this endpoint.
- `/authenticated`: Requires authentication to access this endpoint.
- `/employee`: Requires the user to have the "EMPLOYEE" authority.
- `/admin`: Requires the user to have the "SCOPE_test-resource-server1/admin" authority.

JWT (JSON Web Token) validation is configured with the issuer URI pointing to your AWS Cognito Identity Provider.

You can modify the security configuration as needed in the code to suit your application's requirements.

## Logging

The application uses Spring Security's logging with a TRACE level. You can adjust the log level in the `application.yml` file under the `logging` section according to your debugging needs.

```yaml
logging:
  level:
    org:
      springframework:
        security: TRACE
```


Uses the following URL structure 

```
cognito-idp.{region}.amazonaws.com/{pool_id}/.well-known/openid-configuration
```

For example :
https://cognito-idp.eu-central-1.amazonaws.com/eu-central-1_laH88DAFr/.well-known/openid-configuration


## Configuration

```
spring:
  security:
    oauth2:
      client:
        registration:
          cognito:
            clientId: xxx
            clientSecret: xxx
            scope: openid
            redirect-uri: http://localhost:8080/login/oauth2/code/cognito
            clientName: spring-boot-cognito-oauth2
        provider:
          cognito:
            issuerUri: https://cognito-idp.eu-central-1.amazonaws.com/eu-central-1_laH88DAFr
            user-name-attribute: cognito:username

```

## JWT token

This resource server expects a Bearer token from cognito that looks like this (JWT Token coming from cognito)

```
{
  "sub": "6uqq0pv5s20u1sfg3la0s545c8",
  "token_use": "access",
  "scope": "test-resource-server1/scope2 test-resource-server1/scope1",
  "auth_time": 1700923827,
  "iss": "https://cognito-idp.eu-central-1.amazonaws.com/eu-central-1_7s0YJek0R",
  "exp": 1700927427,
  "iat": 1700923827,
  "version": 2,
  "jti": "808b3f9d-744b-480c-8742-98644563b084",
  "client_id": "6uqq0pv5s20u1sfg3la0s545c8"
}
```

## Under the hood

Some Spring constructs used to validate the Bearer token.

- Because we pass an authentication header with a `Bearer` token, the `BearerTokenAuthenticationFilter` kicks in.
- Through that filter, the `JwtAuthenticationProvider` does the heavy lifting regarding the token.
- Will use the `NimbusJwtDecoder` that will parse / decode / validate the token.
- A collection of `OAuth2TokenValidator` is used to validate the token.
- The `JwtGrantedAuthoritiesConverter` will convert the JWT scopes into grantedAuthorities so they can be used in Spring


## AWS Cognito notes

- if during your client credentials flow you don't specify scopes, all scopes are returned.
- Spring converts `test-resource-server1/admin` into `SCOPE_test-resource-server1/admin`

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
