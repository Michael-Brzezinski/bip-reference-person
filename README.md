## What is this repository for? ##

This is a suite of projects to demonstrate various patterns required to deploy and run application spring boot and spring cloud services on the BIP Platform.  

## Project Breakdown ##

1. **ocp-reference-partner**: Partner services for reference, showing BGS with sample mock data
1. **ocp-reference-person**: Service implementation project.  It has REST endpoints and shows various patterns for producing endpoints, swagger for the application, registering the application with Consul, Secrets from Vault calling REST endpoints through Zuul, Hystrix Circuit Breaker, logging pattern etc.
1. **ocp-reference-inttest**: Contains the integration tests using RestAssured, Cucumber libraries. Includes Test cases against the end points for ascent demo and claims demo. 
1. **ocp-reference-perftest**: Contains the performance JMX tests scripts for Apache JMeter

## How to include and download the dependency framework libraries in your project ##

The projects in this repository are dependent on libraries from [OCP framework](https://github.com/department-of-veterans-affairs/ocp-framework) for  auto configuration, common shared libraries, parent maven configuration and test libary. These libraries can be included as shown below.

       <dependency>
         <groupId>gov.va.ocp.framework</groupId>
         <artifactId>ocp-framework-autoconfigure</artifactId>
         <version><!-- add the appropriate version --></version>
       </dependency>
       <dependency>
         <groupId>gov.va.ocp.framework</groupId>
         <artifactId>ocp-framework-libraries</artifactId>
         <version><!-- add the appropriate version --></version>
       </dependency>
       <dependency>
         <groupId>gov.va.ocp.framework</groupId>
         <artifactId>ocp-framework-parentpom</artifactId>
         <version><!-- add the appropriate version --></version>
       </dependency>
       <dependency>
         <groupId>gov.va.ocp.framework</groupId>
         <artifactId>ocp-framework-test-lib</artifactId>
         <version><!-- add the appropriate version --></version>
       </dependency>

To make these libraries available locally for the service projects to compile and build, there are 2 options.

**OPTION 1**

1. Clone the `ocp-framework` repository `git clone https://github.com/department-of-veterans-affairs/ocp-framework.git`
1. Navigate to the folder `ocp-framework` and run `mvn clean install` command. This would build all the libraries with versions as configured in pom.xml files.

**OPTION 2**

This is a temporary solution until Nexus repository is made available by DevOps. 

A <repositories> section has been added in the reactor pom.xml of this repository. See [pom.xml](https://github.com/department-of-veterans-affairs/ocp-reference-spring-boot/blob/master/pom.xml). To verify library versions, see the [mvn-repo][https://github.com/department-of-veterans-affairs/ocp-framework/branches] feature branch of `ocp-framework`.

You MUST also update your local ~/.m2/settings.xml as shown below.

	<settings>
	  <servers>
	    <server>
	      <id>github</id>
	      <username>{{Your GitHub User Name}}</username>
	      <password>{{Your Personal Access Token}}</password>
	      <configuration>
        	<httpHeaders>
	          	<property>
	            	<name>Authorization</name>
	            	<!--
			For value tag below:
				Step 1: Base64-encode your username and Github access token together
				        in the form: {{username}}:{{access_token}}
					Example: encode the string "myGithubUsername:ab123983245sldfkjsw398r7"
				Step 2: Add the encoded string to the value tag in the form of
					"Basic {{encoded-string}}"
					Example: <value>Basic YXJtaXvB4F5ghTE2OGYwNmExMWM2NDdhYjWExZjQ1N2FhNGJiMjE=</value>
	            	Base64 encoders:
				https://codebeautify.org/base64-encode
				https://www.base64encode.org/
			-->
	            	<value>Basic {{base64 encoded content}}</value>
	          	</property>
        	</httpHeaders>
          </configuration>
	    </server>
	  </servers>
	</settings>

## How to build and test? ##

Follow the link to get started. [Quick Start Guide](docs/quick-start-guide.md)

## Core Concepts and Patterns
* Service Discovery
* [Log and Audit Management](docs/log-audit-management.md)
* [Cache Management](docs/cache-management.md)
* [Swagger Management](docs/swagger-management.md)
* [Application Security Management](docs/application-security-management.md)
* [Hystrix Circuit Breaker Management](docs/hystrix-management.md)
* [Secrets Management](docs/secrets.md)
* [Configuration Management](docs/config-management.md)
* [Deployment Packaging](docs/deployment-package.md)

## Contribution guidelines ## 
* If you or your team wants to contribute to this repository, then fork the repository and follow the steps to create a PR for our upstream repo to review and commit the changes
* [Creating a pull request from a fork](https://help.github.com/articles/creating-a-pull-request-from-a-fork/)

## Local Development
Instructions on running the application local can be found [here](local-dev)
	
