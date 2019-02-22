package gov.va.ocp.reference.framework.security;

import org.apache.ws.security.handler.WSHandlerConstants;
import org.apache.wss4j.common.crypto.Crypto;
import org.apache.wss4j.common.crypto.CryptoFactory;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.slf4j.event.Level;
import org.springframework.ws.soap.security.wss4j2.callback.KeyStoreCallbackHandler;

import gov.va.ocp.reference.framework.constants.AnnotationConstants;
import gov.va.ocp.reference.framework.log.ReferenceBanner;
import gov.va.ocp.reference.framework.log.ReferenceLogger;
import gov.va.ocp.reference.framework.log.ReferenceLoggerFactory;

/**
 * A Wss4j2 Security Interceptor to encrypt secure message header and body.
 * <p>
 * Instantiation of this class requires a {@link #retrieveCryptoProps()} method
 * that returns a {@link CryptoProperties} implementation, as declared in
 * {@link AbstractWss4jSecurityInterceptor#retrieveCryptoProps()}.
 * <p>
 * A complete example can be found in the spring beans of vetservices-partner-efolder EFolderWsClientConfig.java.
 * <p>
 * <b>NOTE:</b> VBMS uses the same cert for ssl AND signing AND key time stamp AND decryption.
 * If future implementations require separate certificates, this code - and possibly the {@link CryptoProperties}
 * interface and certainly its implementations - will need to be modified to provide the additional alias(es).
 * <p>
 * Calling code would typically provide the method in-line during construction, for example:
 *
 * <pre>
 * new DecryptionWss4jSecurityInterceptor() {
 * &#64;Override
 * public CryptoProperties retrieveCryptoProps() {
 * return new DecryptionWss4jSecurityInterceptor interceptor = cryptoProps.retrieveCryptoProperties();
 * }
 * };
 */
public abstract class DecryptionWss4jSecurityInterceptor extends AbstractWss4jSecurityInterceptor {

	private static final ReferenceLogger LOGGER = ReferenceLoggerFactory.getLogger(DecryptionWss4jSecurityInterceptor.class);

	public DecryptionWss4jSecurityInterceptor() {
		try {
			final Crypto crypto = CryptoFactory.getInstance(retrieveCryptoProps());
			setValidationDecryptionCrypto(crypto);
			setValidationSignatureCrypto(crypto);
		} catch (WSSecurityException e) {
			LOGGER.error(ReferenceBanner.newBanner(AnnotationConstants.INTERCEPTOR_EXCEPTION, Level.ERROR), 
					"Error: Decryption Validation Crypto Factory Bean" + e.getMessage(), e);

		}

		this.setValidationActions(
				WSHandlerConstants.ENCRYPT + " " + WSHandlerConstants.TIMESTAMP + " " + WSHandlerConstants.SIGNATURE);
		final KeyStoreCallbackHandler keyStoreCallbackHandler = new KeyStoreCallbackHandler();
		keyStoreCallbackHandler.setPrivateKeyPassword(retrieveCryptoProps().getCryptoKeystorePw());
		this.setValidationCallbackHandler(keyStoreCallbackHandler);
	}
}