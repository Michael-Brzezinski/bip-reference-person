package gov.va.ocp.reference.service.rest.client.discovery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;

/**
 * REST Client class that uses DiscoveryClient api to invoke the service by iterating through
 * the service instances
 *
 * @author prashanthmds
 *
 */

public class DemoUsageDiscoveryClient {
	@Autowired
	private DiscoveryClient discoveryClient;

//	TODO
//	public ServiceInstancesServiceResponse invokeServiceUsingDiscoveryClient() {
//		final ServiceInstancesServiceResponse response = new ServiceInstancesServiceResponse();
//
//		discoveryClient.getServices().forEach((final String service) ->
//		// use discovery service to build out a collection of our ServiceInstanceDetail objects
//		discoveryClient.getInstances(service).forEach((final ServiceInstance serviceInstance) -> {
//			final ServiceInstanceDetail serviceInstanceDetail = new ServiceInstanceDetail();
//			serviceInstanceDetail.setHost(serviceInstance.getHost());
//			serviceInstanceDetail.setPort(Integer.toString(serviceInstance.getPort()));
//			serviceInstanceDetail.setUri(serviceInstance.getUri().toString());
//			serviceInstanceDetail.setServiceId(serviceInstance.getServiceId());
//			serviceInstanceDetail.setMetaData(Arrays.toString(serviceInstance.getMetadata().entrySet().toArray()));
//			response.getServiceInstanceDetails().add(serviceInstanceDetail);
//		}));
//		return response;
//	}
}