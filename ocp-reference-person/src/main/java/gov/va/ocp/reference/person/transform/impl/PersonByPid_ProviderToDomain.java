package gov.va.ocp.reference.person.transform.impl;

import gov.va.ocp.framework.transfer.transform.AbstractProviderToDomain;
import gov.va.ocp.reference.api.person.model.v1.PersonInfoRequest;
import gov.va.ocp.reference.person.model.PersonByPidDomainRequest;

/**
 * Transform a REST Provider {@link PersonInfoRequest} into a service Domain {@link PersonByPidDomainRequest} object.
 *
 * @author aburkholder
 */
public class PersonByPid_ProviderToDomain extends AbstractProviderToDomain<PersonInfoRequest, PersonByPidDomainRequest> {

	/**
	 * Transform a REST Provider {@link PersonInfoRequest} into a service Domain {@link PersonByPidDomainRequest} object.
	 * <p>
	 * {@inheritDoc AbstractProviderToDomain}
	 */
	@Override
	public PersonByPidDomainRequest convert(PersonInfoRequest domainObject) {
		PersonByPidDomainRequest providerObject = new PersonByPidDomainRequest();
		providerObject.setParticipantID(domainObject.getParticipantID());
		return providerObject;
	}

}
