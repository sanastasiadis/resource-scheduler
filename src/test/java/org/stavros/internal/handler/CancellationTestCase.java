package org.stavros.internal.handler;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.stavros.internal.handler.interfaces.InternalMessage;

public class CancellationTestCase {
	
	@Test
	public void testSingleResource1cancel() {
		ResourceScheduler rs = new ResourceScheduler(1);
		rs.send(new MyMessage("1", "ID1"));
		rs.send(new MyMessage("1", "ID2"));
		rs.addCancelledGroup("1");
		rs.send(new MyMessage("1", "ID3"));
		
		List<InternalMessage> messagesToSend1 = rs.getMessagesToSend();
		assertEquals(messagesToSend1.size(), 1);
		
		List<InternalMessage> messagesToSend2 = rs.getMessagesToSend();
		assertEquals(messagesToSend2.size(), 1);
		
		List<InternalMessage> messagesToSend3 = rs.getMessagesToSend();
		assertEquals(messagesToSend3.size(), 0);
	}
	
	@Test
	public void testSingleResource1cancel2groups() {
		ResourceScheduler rs = new ResourceScheduler(1);
		rs.send(new MyMessage("1", "ID1"));
		rs.send(new MyMessage("1", "ID2"));
		rs.addCancelledGroup("1");
		rs.send(new MyMessage("1", "ID3"));
		rs.send(new MyMessage("2", "ID4"));
		
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
	public void testSingleResource2cancel2groups() {
		ResourceScheduler rs = new ResourceScheduler(1);
		rs.send(new MyMessage("1", "ID1"));
		rs.send(new MyMessage("1", "ID2"));
		rs.addCancelledGroup("1");
		rs.send(new MyMessage("1", "ID3"));
		rs.send(new MyMessage("2", "ID4"));
		rs.send(new MyMessage("2", "ID5"));
		rs.addCancelledGroup("2");
		rs.send(new MyMessage("2", "ID6"));
		
		List<InternalMessage> messagesToSend1 = rs.getMessagesToSend();
		assertEquals(messagesToSend1.size(), 1);
		
		List<InternalMessage> messagesToSend2 = rs.getMessagesToSend();
		assertEquals(messagesToSend2.size(), 1);
		
		List<InternalMessage> messagesToSend3 = rs.getMessagesToSend();
		assertEquals(messagesToSend3.size(), 1);
		
		List<InternalMessage> messagesToSend4 = rs.getMessagesToSend();
		assertEquals(messagesToSend4.size(), 1);
		
		List<InternalMessage> messagesToSend5 = rs.getMessagesToSend();
		assertEquals(messagesToSend5.size(), 0);
	}

}
