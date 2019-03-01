package gov.va.ocp.reference.framework.ws.client;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import gov.va.ocp.reference.framework.log.OcpLogger;
import gov.va.ocp.reference.framework.log.OcpLoggerFactory;

/**
 * The base class for Web Service client simulations, containing utility operations, etc. that are likely reusable across such
 * simulators.
 *
 * @author jshrader
 */
public class BaseWsClientSimulator {

	private final static String CHARSET_UTF8 = "UTF-8";

	/** The Constant LOGGER. */
	private static final OcpLogger LOGGER = OcpLoggerFactory.getLogger(BaseWsClientSimulator.class);

	/**
	 * This is not an abstract class, however it is a base class that is not to be instantiated. In this case, it's probably better to
	 * use a private or a protected constructor in order to prevent instantiation than make the class misleadingly abstract.
	 */
	protected BaseWsClientSimulator() {
	}

	/**
	 * Gets the Response from a simulator file.
	 *
	 * @param fileName resource path to file
	 * @return file's contents as string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected static final String getSimulatorResponseByFileName(final String fileName) throws IOException {
		InputStream inputStream = null;
		try {
			inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
			return convertStreamToString(inputStream);
		} catch (final IOException ioe) {
			throw new IOException("Failed to read from simulator response file at resource.", ioe);
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	/**
	 * Gets the Response from a simulator file.
	 *
	 * @param fileName resource path to file
	 * @return file's contents as string
	 */
	protected static final InputStream getSimulatorResponseStreamByFileName(final String fileName) {
		InputStream inputStream = null;
		try {
			inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
		} catch (final Exception ioe) { // NOSONAR intentional wide catch
			LOGGER.error("Failed to read from simulator response file at resource.", ioe);
		}
		return inputStream;
	}

	/**
	 * Gets the Response from a simulator file.
	 *
	 * @param fileName1 the First File Path
	 * @param fileName2 the First File Path
	 * @return the ByteBuffer
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected static final String getSimulatorResponseByFileName(final String fileName1, final String fileName2) throws IOException {

		String content = null;
		try {
			content = getSimulatorResponseByFileName(fileName1);
		} catch (final IOException ex) {
			content = getSimulatorResponseByFileName(fileName2);
			LOGGER.info(ex.getMessage(), ex);
		}

		return content;
	}

	/**
	 * Convert stream to string.
	 *
	 * @param inputStream the in
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static String convertStreamToString(final InputStream inputStream) throws IOException {

		final BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		int result = bufferedInputStream.read();
		while (result != -1) {
			final byte singleByte = (byte) result;
			byteArrayOutputStream.write(singleByte);
			result = bufferedInputStream.read();
		}
		return byteArrayOutputStream.toString(CHARSET_UTF8);
	}

}
