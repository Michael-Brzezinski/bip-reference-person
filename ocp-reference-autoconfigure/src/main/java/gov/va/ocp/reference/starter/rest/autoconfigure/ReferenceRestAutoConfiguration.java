package gov.va.ocp.reference.starter.rest.autoconfigure;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import gov.va.ocp.reference.framework.rest.client.resttemplate.RestClientTemplate;
import gov.va.ocp.reference.framework.rest.provider.RestProviderHttpResponseAspect;
import gov.va.ocp.reference.framework.rest.provider.RestProviderTimerAspect;
import gov.va.ocp.reference.framework.util.Defense;

/**
 * A collection of spring beans used for REST server and/or client operations.
 *
 * Created by rthota on 8/24/17.
 */
@Configuration
public class ReferenceRestAutoConfiguration {

	@Value("${os.reference.rest.client.connection-timeout:20000}")
	private String connectionTimeout;

	/**
	 * Aspect bean of the {@link RestProviderHttpResponseAspect}
	 * (currently executed around auditables and REST controllers).
	 *
	 * @return RestProviderHttpResponseAspect
	 */
	@Bean
	@ConditionalOnMissingBean
	public RestProviderHttpResponseAspect restProviderHttpResponseAspect() {
		return new RestProviderHttpResponseAspect();
	}

	/**
	 * Aspect bean of the {@link RestProviderTimerAspect}
	 * (currently executed around REST controllers).
	 *
	 * @return RestProviderTimerAspect
	 */
	@Bean
	@ConditionalOnMissingBean
	public RestProviderTimerAspect restProviderTimerAspect() {
		return new RestProviderTimerAspect();
	}

	/**
	 * Configure timeouts for the RestTemplate that will be built.
	 *
	 * @param restTemplateBuilder the RestTemplateBuilder to configure
	 */
	private RestTemplateBuilder configureCommon(RestTemplateBuilder restTemplateBuilder) {
		int connTimeoutValue = 0;
		try {
			connTimeoutValue = Integer.valueOf(connectionTimeout);
		} catch (NumberFormatException e) { // NOSONAR intentionally do nothing
			// let the Defense below take care of it
		}
		Defense.state(connTimeoutValue > 0,
				"Invalid settings: Connection Timeout value must be greater than zero.\n"
						+ "  - Ensure spring scan directive includes gov.va.ocp.reference.framework.rest.client.resttemplate;\n"
						+ "  - Application property must be set to non-zero positive integer value: os.reference.rest.client.connection-timeout {} "
						+ connectionTimeout + ".");

		restTemplateBuilder.setConnectTimeout(Duration.ofMillis(connTimeoutValue)); // milliseconds
		return restTemplateBuilder;
	}

	/**
	 * A bean that acts as a {@link RestTemplate} wrapper for executing client REST calls.
	 * <p>
	 * Useful for making non-Feign REST calls (e.g. to external partners, or public URLs)
	 * that are made in partner or library projects.
	 * <p>
	 * Capabilities / Limitations of the returned RestClientTemplate:
	 * <ul>
	 * <li><b>does</b> derive request timeout values from the application properties.
	 * <li>is <b>not</b> load balanced by the spring-cloud LoadBalancerClient.
	 * <li>does <b>not</b> attach the JWT from the current session to the outgoing request.
	 * </ul>
	 *
	 * @return RestClientTemplate
	 */
	@Bean
	@ConditionalOnMissingBean
	public RestClientTemplate restClientTemplate() {
		RestTemplateBuilder builder = new RestTemplateBuilder();
		builder = configureCommon(builder);
		// add intercepter
		builder = builder.interceptors(tokenClientHttpRequestInterceptor());
		return new RestClientTemplate(builder.build());
	}

	/**
	 * A bean for internal purposes, the standard (non-feign) REST request intercepter
	 *
	 * @return TokenClientHttpRequestInterceptor
	 */
	@Bean
	@ConditionalOnMissingBean
	public TokenClientHttpRequestInterceptor tokenClientHttpRequestInterceptor() {
		return new TokenClientHttpRequestInterceptor();
	}

}
