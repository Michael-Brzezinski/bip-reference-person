package gov.va.ocp.reference.person.transform.impl;

import gov.va.ocp.framework.transfer.transform.AbstractDomainToProvider;
import gov.va.ocp.reference.api.person.model.v1.PersonInfo;
import gov.va.ocp.reference.api.person.model.v1.PersonInfoResponse;
import gov.va.ocp.reference.person.model.PersonByPidDomainResponse;

/**
 * Transform a service Domain {@link PersonByPidDomainResponse} into a REST Provider {@link PersonInfoResponse} object.
 *
 * @author aburkholder
 */
public class PersonByPid_DomainToProvider extends AbstractDomainToProvider<PersonByPidDomainResponse, PersonInfoResponse> {

	/**
	 * Transform a service Domain {@link PersonByPidDomainResponse} into a REST Provider {@link PersonInfoResponse} object.
	 * <br/>
	 * <b>Member objects inside the returned object may be {@code null}.</b>
	 * <p>
	 * {@inheritDoc AbstractDomainToProvider}
	 */
	@Override
	public PersonInfoResponse convert(PersonByPidDomainResponse domainObject) {
		PersonInfoResponse providerObject = new PersonInfoResponse();

		// add data
		PersonInfo providerData = new PersonInfo();
		if (domainObject != null && domainObject.getPersonInfo() != null) {
			providerData.setFileNumber(domainObject.getPersonInfo().getFileNumber());
			providerData.setFirstName(domainObject.getPersonInfo().getFirstName());
			providerData.setLastName(domainObject.getPersonInfo().getLastName());
			providerData.setMiddleName(domainObject.getPersonInfo().getMiddleName());
			providerData.setParticipantId(domainObject.getPersonInfo().getParticipantId());
			providerData.setSocSecNo(domainObject.getPersonInfo().getSocSecNo());
		}
		providerObject.setPersonInfo(providerData);
		// add messages
		if (domainObject.getMessages() != null && !domainObject.getMessages().isEmpty()) {
			for (gov.va.ocp.framework.messages.ServiceMessage domainMsg : domainObject.getMessages()) {
				providerObject.addMessage(domainMsg.getSeverity(), domainMsg.getKey(), domainMsg.getText(),
						domainMsg.getHttpStatus());
			}
		}

		return providerObject;
	}
}
