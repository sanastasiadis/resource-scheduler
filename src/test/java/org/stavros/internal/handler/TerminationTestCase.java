package org.stavros.internal.handler;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.stavros.internal.handler.interfaces.InternalMessage;
import org.stavros.internal.handler.interfaces.MessageType;

public class TerminationTestCase {
	
	@Test
	public void testSingleResource2messages() {
		ResourceScheduler rs = new ResourceScheduler(new MyGateway(), 1);
		rs.send(new MyMessage("1", "ID1"));
		rs.send(new MyMessage(MessageType.TERMINATION, "1", "ID2"));
		
		List<InternalMessage> messagesToSend1 = rs.getMessagesToSend();
		assertEquals(messagesToSend1.size(), 1);
		
		List<InternalMessage> messagesToSend2 = rs.getMessagesToSend();
		assertEquals(messagesToSend2.size(), 1);
		
		List<InternalMessage> messagesToSend3 = rs.getMessagesToSend();
		assertEquals(messagesToSend3.size(), 0);
	}
	
	@Test
	public void testSingleResource1group() {
		ResourceScheduler rs = new ResourceScheduler(new MyGateway(), 1);
		rs.send(new MyMessage("1", "ID1"));
		rs.send(new MyMessage(MessageType.TERMINATION, "1", "ID2"));
		try {
			rs.send(new MyMessage("1", "ID3"));
			assertTrue(false);
		}
		catch(IllegalStateException ise) {
			assertTrue(true);
		}
		
		List<InternalMessage> messagesToSend1 = rs.getMessagesToSend();
		assertEquals(messagesToSend1.size(), 1);
		
		List<InternalMessage> messagesToSend2 = rs.getMessagesToSend();
		assertEquals(messagesToSend2.size(), 1);
		
		List<InternalMessage> messagesToSend3 = rs.getMessagesToSend();
		assertEquals(messagesToSend3.size(), 0);
	}
	
	@Test
	public void testSingleResource2groups() {
		ResourceScheduler rs = new ResourceScheduler(new MyGateway(), 1);
		rs.send(new MyMessage("1", "ID1"));
		rs.send(new MyMessage(MessageType.TERMINATION, "1", "ID2"));
		rs.send(new MyMessage("2", "ID3"));
		
		List<InternalMessage> messagesToSend1 = rs.getMessagesToSend();
		assertEquals(messagesToSend1.size(), 1);
		
		List<InternalMessage> messagesToSend2 = rs.getMessagesToSend();
		assertEquals(messagesToSend2.size(), 1);
		
		List<InternalMessage> messagesToSend3 = rs.getMessagesToSend();
		assertEquals(messagesToSend3.size(), 1);
		
		List<InternalMessage> messagesToSend4 = rs.getMessagesToSend();
		assertEquals(messagesToSend4.size(), 0);
	}
	
	@Test
	public void testSingleResource3groupsTerminations() {
		ResourceScheduler rs = new ResourceScheduler(new MyGateway(), 1);
		rs.send(new MyMessage("1", "ID1"));
		rs.send(new MyMessage(MessageType.TERMINATION, "1", "ID2"));
		rs.send(new MyMessage("2", "ID3"));
		rs.send(new MyMessage(MessageType.TERMINATION, "2", "ID4"));
		try {
			rs.send(new MyMessage("1", "ID5"));
			assertTrue(false);
		}
		catch(IllegalStateException ise) {
			assertTrue(true);
		}
		try {
			rs.send(new MyMessage("2", "ID6"));
			assertTrue(false);
		}
		catch(IllegalStateException ise) {
			assertTrue(true);
		}
		rs.send(new MyMessage("3", "ID7"));
		rs.send(new MyMessage(MessageType.TERMINATION, "3", "ID8"));
		try {
			rs.send(new MyMessage("3", "ID9"));
			assertTrue(false);
		}
		catch(IllegalStateException ise) {
			assertTrue(true);
		}
		
		List<InternalMessage> messagesToSend1 = rs.getMessagesToSend();
		assertEquals(messagesToSend1.size(), 1);
		
		List<InternalMessage> messagesToSend2 = rs.getMessagesToSend();
		assertEquals(messagesToSend2.size(), 1);
		
		List<InternalMessage> messagesToSend3 = rs.getMessagesToSend();
		assertEquals(messagesToSend3.size(), 1);
		
		List<InternalMessage> messagesToSend4 = rs.getMessagesToSend();
		assertEquals(messagesToSend4.size(), 1);
		
		List<InternalMessage> messagesToSend5 = rs.getMessagesToSend();
		assertEquals(messagesToSend5.size(), 1);
		
		List<InternalMessage> messagesToSend6 = rs.getMessagesToSend();
		assertEquals(messagesToSend6.size(), 1);
		
		List<InternalMessage> messagesToSend7 = rs.getMessagesToSend();
		assertEquals(messagesToSend7.size(), 0);
	}

}
