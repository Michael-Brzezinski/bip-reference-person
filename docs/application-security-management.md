# Application Security Management

## Capability and Features
- Self contained Secure Token for Authentication
- Stateless Authentication
- Trusted Partner
- Server to Server communication

## Security Pattern
- [JSON Web Token](https://tools.ietf.org/html/rfc7519) ([JWT](https://jwt.io/)) Security using Transitive Trust Relationship to be enabled for the application
- JWT signature algorithm to sign the token must use HS256 as defined in [JSON Web Algorithms](https://tools.ietf.org/html/draft-ietf-jose-json-web-algorithms-31#section-3.1)
- JWT needs to be signed with a signing key and must match for the trusted services that communicates.
- JWT issuer to exists in the parsed JWT as specified in "[iss](https://tools.ietf.org/html/rfc7519#section-4.1.1)" and must match for the trusted services that communicates.
- BIP Framework library would DeCrypt the JWT and attempt to construct a PersonTraits object from it. [PersonTraits.java](https://github.com/department-of-veterans-affairs/bip-framework/blob/master/bip-framework-libraries/src/main/java/gov/va/bip/framework/security/PersonTraits.java)

## Security configuration
- Add dependency in pom.xml to include bip-framework-autoconfigure library

```xml
      <dependency>
        <groupId>gov.va.bip.framework</groupId>
        <artifactId>bip-framework-autoconfigure</artifactId>
        <version><!-- add the appropriate version --></version>
      </dependency>
```

 - Java source file from BIP Framework library that does AutoConfiguration for JWT. [BipSecurityAutoConfiguration.java](https://github.com/department-of-veterans-affairs/bip-framework/blob/master/bip-framework-autoconfigure/src/main/java/gov/va/bip/framework/security/autoconfigure/BipSecurityAutoConfiguration.java)

 - JWT authentication properties are set via prefix "bip.framework.security.jwt". Java source file: [JwtAuthenticationProperties.java](https://github.com/department-of-veterans-affairs/bip-framework/blob/master/bip-framework-libraries/src/main/java/gov/va/bip/framework/security/jwt/JwtAuthenticationProperties.java)

 - By default in ${spring.config.name}.yml file, BIP framework security is enabled for all the application profiles. To disable the security, developer would have to set the property bip.framework.security.jwt.enabled to false

    An Example from bip-reference-person service is shown below.
    ```yml
	###############################################################################
	#Property configuration to enable or disable JWT security for the service calls
	#JWT security is enabled by default. So to disable it you have to
	#set the environment system property os.reference.security.jwt.enabled=false
	###############################################################################
	bip.framework:
	  security:
	    jwt:
	      enabled: true
	      filterProcessUrls:
	       - /api/v1/persons/**
	       - /api/v2/persons/**
	      excludeUrls:
	       - /**
   ```
- By default [TokenResource](https://github.com/department-of-veterans-affairs/bip-framework/blob/master/bip-framework-libraries/src/main/java/gov/va/bip/framework/security/jwt/TokenResource.java) java bean with `/token` end point is enabled for all the application profiles. To disable this end point, you can set `bip.framework.security.jwt.generate.enabled: false`. This end point generates a valid JWT token with default `900` seconds expiration time. This property can be overridden as shown `bip.framework.security.jwt.expireInSeconds: 900` 

-  All the properties for JWT Security with prefix `bip.framework.security.jwt` that are configurable are listed below.

     **bip.framework.security.jwt.enabled**: Boolean property to enable or disable JWT security on the service end points. Defaults to "**true**"

     **bip.framework.security.jwt.filterProcessUrls**: List of strings to specify the URLs that are allowed for any authenticated user. Defaults to "**/api/****"

     **bip.framework.security.jwt.excludeUrls**: List of strings that allows adding RequestMatcher instances which Spring Security should ignore. Defaults to "**/****"

     **bip.framework.security.jwt.issuer**: Ensures that the specified "iss" exists in the parsed JWT. If missing or if the parsed value does not equal the specified value, an exception will be thrown indicating that the JWT is 								 invalid and may not be used. Defaults to "**Vets.gov**"

     **bip.framework.security.jwt.secret**: JWT needs to be signed with a signing key and must match for the trusted services that communicates. Defaults to "**secret**"

     **bip.framework.security.jwt.header**: Request header name to read JWT token value from. Defaults to "**Authorization**"

     **bip.framework.security.jwt.validation.required-parameters**: Specifies which fields in the `PersonTraits` object must contain a value. Fields are specified by setting this property value to a comma-separated list of field names. If the JWT person object does not have a value in the specified field(s), the request will be rejected.

## Retrieving Person Traits from Security Context

Information about the logged in individual is transmitted as part of the encrypted token. BIP applications can retrieve this information from the frameworks `PersonTraits` object.

Because the values of the Person Traits are derived by other authorities in the VA, it isn't permissible to alter them directly, so the framework doesn't provide any means to do so.

```java
	import gov.va.bip.framework.security.PersonTraits;
	import gov.va.bip.framework.security.SecurityUtils;

	public class SomeClass {
		PersonTraits personTraits = SecurityUtils.getPersonTraits();
	}
```
