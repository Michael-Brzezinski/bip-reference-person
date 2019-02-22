package gov.va.os.reference.framework.validation;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Interface of validatable objects
 * 
 * @author jshrader
 */
@FunctionalInterface
public interface Validatable extends Serializable {

	/**
	 * Validate.
	 *
	 * @param messages the messages
	 * @return the map
	 */
	Map<String, List<ViolationMessageParts>> validate(Map<String, List<ViolationMessageParts>> messages);

}