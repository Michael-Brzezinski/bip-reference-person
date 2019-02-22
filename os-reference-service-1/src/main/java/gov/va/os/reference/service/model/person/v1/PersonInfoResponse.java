package gov.va.os.reference.service.model.person.v1;

import gov.va.os.reference.framework.service.ServiceResponse;
import gov.va.os.reference.framework.transfer.ServiceTransferObjectMarker;

/**
 * A class to represent the data contained in the response
 * from the Person Service.
 *
 */
public class PersonInfoResponse extends ServiceResponse implements ServiceTransferObjectMarker {

	/** Id for serialization. */
	private static final long serialVersionUID = 7327802119014249445L;

	/** A PersonInfo instance. */
	private PersonInfo personInfo;

	/**
	 * Gets the person info.
	 *
	 * @return A PersonInfo instance
	 */
	public final PersonInfo getPersonInfo() {
		return personInfo;
	}

	/**
	 * Sets the person info.
	 *
	 * @param personInfo A PersonInfo instance
	 */
	public final void setPersonInfo(final PersonInfo personInfo) {
		this.personInfo = personInfo;
	}
}
