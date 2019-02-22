package gov.va.os.reference.partner.person.ws.client;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import gov.va.os.reference.partner.person.ws.transfer.FindPersonByPtcpntId;
import gov.va.os.reference.partner.person.ws.transfer.FindPersonByPtcpntIdResponse;
import gov.va.os.reference.partner.person.ws.transfer.FindPersonBySSN;
import gov.va.os.reference.partner.person.ws.transfer.FindPersonBySSNResponse;
import gov.va.os.reference.framework.audit.AuditEvents;
import gov.va.os.reference.framework.audit.Auditable;
import gov.va.os.reference.framework.config.ReferenceCommonSpringProfiles;
import gov.va.os.reference.framework.util.Defense;
import gov.va.os.reference.framework.ws.client.BaseWsClientImpl;

/**
 * This class implements the Person WS Client interface. It encapsulates the details of interacting with the Person Web Service.
 * 
 */
@Component(PersonWsClientImpl.BEAN_NAME)
@Profile({ReferenceCommonSpringProfiles.PROFILE_REMOTE_CLIENT_IMPLS,
		PersonWsClient.PROFILE_PERSONWSCLIENT_REMOTE_CLIENT_IMPL})
public class PersonWsClientImpl extends BaseWsClientImpl implements PersonWsClient {

	/** A constant representing the Spring Bean name. */
	public static final String BEAN_NAME = "personWsClient";

	/**
	 * Spring axiom web service template.
	 */
	@Autowired
	@Qualifier("personWsClient.axiom")
	private WebServiceTemplate axiomWebServiceTemplate;

	/**
	 * The WebServiceTemplate can't be null.
	 */
	@PostConstruct
	public final void postConstruct() {
		Defense.notNull(axiomWebServiceTemplate, "axiomWebServiceTemplate cannot be null in order for "
				+ this.getClass().getSimpleName() + " to work properly.");
	}

	@SuppressWarnings("unchecked")
	// for webServiceResponse Object cast
	// this method cannot be marked final as axiomWebServiceTemplate will be null at runtime.
	@Override
	@Auditable(event = AuditEvents.REQUEST_RESPONSE, activity = "partnerPersonInfo")
	public JAXBElement<FindPersonBySSNResponse> getPersonInfo(final JAXBElement<FindPersonBySSN> findPersonBySSNRequest) {

		Defense.notNull(findPersonBySSNRequest, REQUEST_FOR_WEBSERVICE_CALL_NULL);
		final Object webServiceResponse = axiomWebServiceTemplate.marshalSendAndReceive(findPersonBySSNRequest);
		Defense.notNull(webServiceResponse, RESPONSE_FROM_WEBSERVICE_CALL_NULL);
		return (JAXBElement<FindPersonBySSNResponse>) webServiceResponse;
	}

	@SuppressWarnings("unchecked")
	// this method cannot be marked final as axiomWebServiceTemplate will be null at runtime.
	@Override
	@Auditable(event = AuditEvents.REQUEST_RESPONSE, activity = "partnerPersonInfoByPtcpntId")
	public JAXBElement<FindPersonByPtcpntIdResponse> getPersonInfoByPtcpntId(
			final JAXBElement<FindPersonByPtcpntId> findPersonByPtcpntIdRequest) {
		
		Defense.notNull(findPersonByPtcpntIdRequest, REQUEST_FOR_WEBSERVICE_CALL_NULL);
		final Object webServiceResponse = axiomWebServiceTemplate.marshalSendAndReceive(findPersonByPtcpntIdRequest);
		Defense.notNull(webServiceResponse, RESPONSE_FROM_WEBSERVICE_CALL_NULL);
		return (JAXBElement<FindPersonByPtcpntIdResponse>) webServiceResponse;
	}

}