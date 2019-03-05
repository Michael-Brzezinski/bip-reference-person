package gov.va.ocp.reference.starter.rest.autoconfigure;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.config.ConnectionConfig;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import gov.va.ocp.reference.framework.rest.client.exception.ResponseEntityErrorHandler;
import gov.va.ocp.reference.framework.rest.client.resttemplate.RestClientTemplate;
import gov.va.ocp.reference.framework.rest.provider.aspect.RestProviderHttpResponseAspect;
import gov.va.ocp.reference.framework.rest.provider.aspect.RestProviderTimerAspect;
import gov.va.ocp.reference.framework.util.Defense;

/**
 * A collection of spring beans used for REST server and/or client operations.
 *
 * Created by rthota on 8/24/17.
 * @author akulkarni
 */
@Configuration
public class OcpRestAutoConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(OcpRestAutoConfiguration.class);

	@Value("${ocp.rest.client.connectionTimeout:20000}")
	private String connectionTimeout;

	@Value("${ocp.rest.client.readTimeout:30000}")
	private String readTimeout;

	@Value("${ocp.rest.client.maxTotalPool:10}")
	private String maxTotalPool;

	@Value("${ocp.rest.client.defaultMaxPerRoutePool:5}")
	private String defaultMaxPerRoutePool;

	@Value("${ocp.rest.client.validateAfterInactivityPool:10000}")
	private String validateAfterInactivityPool;

	@Value("${ocp.rest.client.connectionBufferSize:4128}")
	private String connectionBufferSize;
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
	 * Http components client http request factory.
	 *
	 * @return the http components client http request factory
	 */
	@Bean
	public HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory() {
		int connTimeoutValue = 0;
		try {
			connTimeoutValue = Integer.valueOf(connectionTimeout);
		} catch (NumberFormatException e) { // NOSONAR intentionally do nothing
			// let the Defense below take care of it
		}
		Defense.state(connTimeoutValue > 0,
				"Invalid settings: Connection Timeout value must be greater than zero.\n"
						+ "  - Ensure spring scan directive includes gov.va.ocp.reference.framework.rest.client.resttemplate;\n"
						+ "  - Application property must be set to non-zero positive integer value: ocp.rest.client.connection-timeout {} "
						+ connectionTimeout + ".");

		ConnectionConfig connectionConfig = ConnectionConfig.custom()
				.setBufferSize(Integer.valueOf(connectionBufferSize))
				.build();
		HttpClientBuilder clientBuilder = HttpClients.custom();
		PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager();
		poolingConnectionManager.setMaxTotal(Integer.valueOf(maxTotalPool));
		poolingConnectionManager.setDefaultMaxPerRoute(Integer.valueOf(defaultMaxPerRoutePool));
		poolingConnectionManager.setValidateAfterInactivity(Integer.valueOf(validateAfterInactivityPool));

		clientBuilder.setConnectionManager(poolingConnectionManager);
		clientBuilder.setDefaultConnectionConfig(connectionConfig);
		clientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(3, true, new ArrayList<>()) {
			@Override
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				LOGGER.info("Retry request, execution count: {}, exception: {}", executionCount, exception);
				if (exception instanceof org.apache.http.NoHttpResponseException) {
	                LOGGER.warn("No response from server on " + executionCount + " call");
	                return true;
	            }
				return super.retryRequest(exception, executionCount, context);
			}

		});
		
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
				new HttpComponentsClientHttpRequestFactory(clientBuilder.build());
		clientHttpRequestFactory.setConnectTimeout(connTimeoutValue);
		clientHttpRequestFactory.setReadTimeout(Integer.valueOf(readTimeout));
		return clientHttpRequestFactory;
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
		RestTemplate restTemplate = new RestTemplate(httpComponentsClientHttpRequestFactory());
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
		interceptors.add(tokenClientHttpRequestInterceptor());
		restTemplate.setInterceptors(interceptors);
		restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(httpComponentsClientHttpRequestFactory()));
		restTemplate.getMessageConverters().stream().filter(StringHttpMessageConverter.class::isInstance).map(StringHttpMessageConverter.class::cast).forEach(a -> {
			a.setWriteAcceptCharset(false);
			a.setDefaultCharset(StandardCharsets.UTF_8);
		});
		// create error handler
		ResponseEntityErrorHandler errorHandler = new ResponseEntityErrorHandler();
		errorHandler.setMessageConverters(restTemplate.getMessageConverters());
		restTemplate.setErrorHandler(errorHandler);
		return new RestClientTemplate(restTemplate);
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
