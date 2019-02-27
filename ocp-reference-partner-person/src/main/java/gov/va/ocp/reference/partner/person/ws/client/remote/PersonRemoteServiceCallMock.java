package gov.va.ocp.reference.partner.person.ws.client.remote;

import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import gov.va.ocp.reference.framework.config.ReferenceCommonSpringProfiles;
import gov.va.ocp.reference.framework.log.ReferenceLogger;
import gov.va.ocp.reference.framework.log.ReferenceLoggerFactory;
import gov.va.ocp.reference.framework.security.PersonTraits;
import gov.va.ocp.reference.framework.security.SecurityUtils;
import gov.va.ocp.reference.framework.transfer.PartnerTransferObjectMarker;
import gov.va.ocp.reference.framework.util.Defense;
import gov.va.ocp.reference.framework.ws.client.remote.AbstractRemoteServiceCallMock;
import gov.va.ocp.reference.framework.ws.client.remote.RemoteServiceCall;
import gov.va.ocp.reference.partner.person.ws.client.PersonWsClientException;
import gov.va.ocp.reference.partner.person.ws.transfer.FindPersonByPtcpntId;

/**
 * Implements the {@link RemoteServiceCall} interface, and extends
 * {@link AbstractRemoteServiceCallMock} for mocking the remote client under the
 * simulators spring profile.
 */
@Profile(ReferenceCommonSpringProfiles.PROFILE_REMOTE_CLIENT_SIMULATORS)
@Component(PersonRemoteServiceCallImpl.BEAN_NAME) // intentionally using the IMPL name
public class PersonRemoteServiceCallMock extends AbstractRemoteServiceCallMock {
	/** Logger */
	private static final ReferenceLogger LOGGER = ReferenceLoggerFactory.getLogger(PersonRemoteServiceCallMock.class);

	/** error message if request is null */
	static final String ERROR_NULL_REQUEST = "getKeyForMockResponse request parameter cannot be null.";

	// TODO
	/** Error message prefix if request type is not handled in getKeyForMockResponse(..) */
	static final String ERROR_UNHANDLED_REQUEST_TYPE =
			PersonRemoteServiceCallMock.class.getSimpleName()
					+ ".getKeyForMockResponse(..) does not have a file naming block for requests of type ";

	/*
	 * Below: Constants for mock XML file names
	 */

	/** The {@code src/main/resources/test/mocks/*} filename prefix for the "filename.PID.xml" mock file. */
	static final String MOCK_FINDPERSONBYPTCPNTID_RESPONSE = "person.getPersonInfoByPtcpntId.{0}";

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * gov.va.ascent.framework.ws.client.remote.RemoteServiceCall#callRemoteService(
	 * org.springframework.ws.client.core. WebServiceTemplate,
	 * gov.va.ascent.framework.transfer.PartnerTransferObjectMarker, java.lang.Class)
	 */
	@Override
	public PartnerTransferObjectMarker callRemoteService(final WebServiceTemplate webserviceTemplate,
			final PartnerTransferObjectMarker request, final Class<? extends PartnerTransferObjectMarker> requestClass) {
		Defense.notNull(request, "Cannot callRemoteService with null request");
		Defense.notNull(requestClass, "Cannot callRemoteService with null requestClass");

		LOGGER.info("Calling MOCK service with request " + ReflectionToStringBuilder.reflectionToString(request));
		// super handles exceptions
		PartnerTransferObjectMarker response = super.callMockService(webserviceTemplate, request, requestClass);
		LOGGER.info("Called MOCK service with request '" + requestClass.getSimpleName() + "', got mocked response '"
				+ response.getClass() + "'");
		return response;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.va.ocp.reference.framework.ws.client.remote.AbstractRemoteServiceCallMock#
	 * getKeyForMockResponse(gov.va.ascent.framework.transfer.
	 * PartnerTransferObjectMarker)
	 */
	@Override
	protected String getKeyForMockResponse(final PartnerTransferObjectMarker request) {
		Defense.notNull(request, ERROR_NULL_REQUEST);

		String mockFilename = null;

		// TODO
		if (request.getClass().isAssignableFrom(FindPersonByPtcpntId.class)) {
			mockFilename = getFileName(MOCK_FINDPERSONBYPTCPNTID_RESPONSE);

		} else {
			throw new PersonWsClientException(
					this.getClass().getSimpleName() + ERROR_UNHANDLED_REQUEST_TYPE + request.getClass().getName());
		}

		// return value can never be null or empty, there is Defense against it
		return mockFilename;
	}

	/**
	 * Get filename from a pattern.
	 * It is assumed the pattern will always be in the form of <tt>servicename.operation[.{0}]</tt>.
	 * <p>
	 * Determining replaceable params looks for "{" in the fileNamePattern,
	 * and requires the security PersonTraits to be populated correctly.
	 * <p>
	 * If any of these checks fails, the fileNamePattern is returned with the trailing ".{0}" removed.
	 *
	 * @param fileNamePattern - the filename, or if param needs replacing, the pattern
	 * @param replaceableParam - null or the param replacement value
	 * @return String a filename
	 */
	private String getFileName(final String fileNamePattern) {
		Defense.notNull(fileNamePattern, "fileNamePattern cannot be null");
		String fileName = fileNamePattern;
		final PersonTraits personTraits = SecurityUtils.getPersonTraits();

		if (personTraits != null
				&& StringUtils.isNotBlank(personTraits.getPid())
				&& fileName.contains("{")) {
			fileName = MessageFormat.format(fileName, personTraits.getPid());
			LOGGER.warn("Could not retrieve PersonTraits from SecurityContext. Defaulting to MOCK response " + fileName);
		} else {
			if (fileName.contains("{")) {
				fileName = fileName.replace(".{0}", "");
			}

		}
		return fileName;
	}

}
