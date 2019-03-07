package gov.va.ocp.framework.rest.provider.bre.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import gov.va.ocp.framework.messages.MessageSeverity;
import gov.va.ocp.framework.rest.provider.Message;

public class MessageSeverityMatchRuleTest {

	MessageSeverityMatchRule messageSeverityMatchRule;
	Set<Message> messagesToEval = new HashSet<Message>();

	@Before
	public void setUp() throws Exception {
		Message errMessage = new Message(MessageSeverity.ERROR.name(), "ErrorKey", "Error Text", null);
		Message fatalMessage = new Message(MessageSeverity.FATAL.name(), "FatalKey", "Fatal Error Text", null);
		Message warnMessage = new Message(MessageSeverity.WARN.name(), "WarnKey", "Warn Text", null);
		Message infoMessage = new Message(MessageSeverity.INFO.name(), "InfoKey", "Info Text", null);
		messagesToEval.add(errMessage);
		messagesToEval.add(fatalMessage);
		messagesToEval.add(warnMessage);
		messagesToEval.add(infoMessage);
	}

	@After
	public void tearDown() throws Exception {
		messagesToEval.clear();
	}

	// TODO
//	@Test
//	public void testEval() {
//		messageSeverityMatchRule = new MessageSeverityMatchRule(MessageSeverity.ERROR, HttpStatus.UNAUTHORIZED);
//		assertEquals(HttpStatus.UNAUTHORIZED, messageSeverityMatchRule.eval(messagesToEval));
//	}

	@Test
	public void testEvalMessagesNull() {
		messageSeverityMatchRule = new MessageSeverityMatchRule(MessageSeverity.ERROR, HttpStatus.UNAUTHORIZED);
		assertEquals(null, messageSeverityMatchRule.eval(null));
	}

	// TODO
//	@Test
//	public void testEvalMessagesEmpty() {
//		messageSeverityMatchRule = new MessageSeverityMatchRule(MessageSeverity.ERROR, HttpStatus.UNAUTHORIZED);
//		assertEquals(null, messageSeverityMatchRule.eval(new HashSet<Message>()));
//	}
//
//	@Test
//	public void testEvalMessagesDoNotMatch() {
//		Message msg = new Message(MessageSeverity.INFO, "InfoKey", "Info Text", null);
//		Set<Message> serviceMessages = new HashSet<>();
//		serviceMessages.add(msg);
//		messageSeverityMatchRule = new MessageSeverityMatchRule(MessageSeverity.ERROR, HttpStatus.UNAUTHORIZED);
//		assertEquals(null, messageSeverityMatchRule.eval(serviceMessages));
//	}

	@Test
	public void testToString() {
		messageSeverityMatchRule = new MessageSeverityMatchRule(MessageSeverity.ERROR, HttpStatus.UNAUTHORIZED);
		assertNotNull(messageSeverityMatchRule.toString());
	}

}
