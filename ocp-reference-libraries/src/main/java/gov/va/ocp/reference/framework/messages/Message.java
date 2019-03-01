package gov.va.ocp.reference.framework.messages;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Message is a generic abstraction of a "message" or "notification" which is layer agnostic and can be used to communicate status or
 * other sorts of information during method calls between components/layers. This is serializable and can be used in SOAP or REST
 * calls.
 *
 * @author jshrader
 */
public class Message extends AbstractMessage {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1711431368372127555L;

	/**
	 * The text is excluded from equals and hash as the key+severity are to jointly indicate a unique message. The text is supplemental
	 * information.
	 */
	private static final String[] EQUALS_HASH_EXCLUDE_FIELDS = new String[] { "text" };

	/** The key. */
	@XmlElement(required = true)
	@NotNull
	private String key;

	/** The message. */
	private String text;

	/** The Http status. */
	private HttpStatus status;

	/** The message severity. */
	@XmlElement(required = true)
	@NotNull
	private MessageSeverity severity;

	/**
	 * Instantiates a new message.
	 */
	public Message() { // NOSONAR @NotNull is a validation annotation, not a usage annotation
		super(); // NOSONAR @NotNull is a validation annotation, not a usage annotation
	} // NOSONAR @NotNull is a validation annotation, not a usage annotation

	/**
	 * Instantiates a new message.
	 *
	 * @param severity the severity for the cause of the message
	 * @param key the key representing the "error code" for the message
	 * @param text the text of the message
	 * @param httpStatus the http status associated with the cause of the message
	 */
	public Message(final MessageSeverity severity, final String key, final String text, final HttpStatus httpStatus) {
		super();
		this.severity = severity;
		this.key = key;
		this.text = text;
		this.status = httpStatus;
	}

	/**
	 * Instantiates a new message providing only replaceable parameters for the message text.
	 *
	 * @param paramCount the number of elements in the name and value arrays
	 * @param paramNames the names, in same order as thier respective getParamValues
	 * @param paramValues the values, in same order as their respective getParamNames
	 */
	public Message(Integer paramCount, String[] paramNames, String[] paramValues) {
		super(paramCount, paramNames, paramValues);
	}

	/**
	 * Instantiates a new message, populating all available fields.
	 *
	 * @param severity the severity for the cause of the message
	 * @param key the key representing the "error code" for the message
	 * @param text the text of the message
	 * @param httpStatus the http status associated with the cause of the message
	 * @param paramCount the number of elements in the name and value arrays
	 * @param paramNames the names, in same order as thier respective getParamValues
	 * @param paramValues the values, in same order as their respective getParamNames
	 */
	public Message(final MessageSeverity severity, final String key, final String text, HttpStatus status,
			Integer paramCount, String[] paramNames, String[] paramValues) {
		super(paramCount, paramNames, paramValues);
		this.severity = severity;
		this.key = key;
		this.text = text;
		this.status = status;
	}

	/**
	 * Gets the key.
	 *
	 * @return the key
	 */
	public final String getKey() {
		return this.key;
	}

	/**
	 * Sets the key.
	 *
	 * @param key the new key
	 */
	public final void setKey(final String key) {
		this.key = key;
	}

	/**
	 * Gets the HttpStatus.
	 *
	 * @return the HttpStatus
	 */
	@JsonProperty("status")
	public String getStatusString() {
		// Since this method is used by introspection based serialisation, it would need to return the status code number instead of
		// the default (enum name), which is why the toString() method is used
		return status == null ? null : status.toString();
	}

	/**
	 * Gets the HttpStatus.
	 *
	 * @return the HttpStatus
	 */
	@JsonIgnore
	public HttpStatus getStatusEnum() {
		return status;
	}

	/**
	 * Sets the HttpStatus.
	 *
	 * @param key the new HttpStatus
	 */
	public void setStatus(final HttpStatus status) {
		this.status = status;
	}

	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public final String getText() {
		return this.text;
	}

	/**
	 * Sets the text.
	 *
	 * @param text the new text
	 */
	public final void setText(final String text) {
		this.text = text;
	}

	/**
	 * Gets the message severity.
	 *
	 * @return the message severity
	 */
	public final MessageSeverity getSeverity() {
		return this.severity;
	}

	/**
	 * Sets the message severity.
	 *
	 * @param severity the new message severity
	 */
	public final void setSeverity(final MessageSeverity severity) {
		this.severity = severity;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.va.ocp.reference.framework.transfer.AbstractTransferObject#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj, EQUALS_HASH_EXCLUDE_FIELDS);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.va.ocp.reference.framework.transfer.AbstractTransferObject#hashCode()
	 */
	@Override
	public final int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, EQUALS_HASH_EXCLUDE_FIELDS);
	}

}
