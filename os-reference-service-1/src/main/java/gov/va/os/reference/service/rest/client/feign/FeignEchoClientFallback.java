package gov.va.os.reference.service.rest.client.feign;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import gov.va.os.reference.service.api.v1.transfer.EchoHostServiceResponse;
import gov.va.os.reference.framework.messages.MessageSeverity;

@Component
/**
 * This class provides the Hystrix fallback implementation for Feign Client calls to the service
 * @author 
 *
 */
public class FeignEchoClientFallback implements FeignEchoClient {

	@Override
	public ResponseEntity<EchoHostServiceResponse> echo() {
		EchoHostServiceResponse response = new EchoHostServiceResponse();
		response.addMessage(MessageSeverity.FATAL, "SERVICE_NOT_AVAILABLE", "This is feign fallback handler, the service wasn't available");
		return new ResponseEntity<EchoHostServiceResponse>(response, HttpStatus.SERVICE_UNAVAILABLE);
	}

}