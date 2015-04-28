package org.stavros.internal.handler;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.stavros.external.gateway.interfaces.Message;

public class ResourceHandlerTestCase {
	
	@Test
	public void testNoResource2messages() {
		ResourceScheduler rs = new ResourceScheduler(0);
		rs.send(new MyMessage(1, "ID1"));
		rs.send(new MyMessage(1, "ID2"));
		
		List<Message> messagesToSend1 = rs.getMessagesToSend();
		assertEquals(messagesToSend1.size(), 0);
	}
	
	@Test
	public void testSingleResource1message() {
		ResourceScheduler rs = new ResourceScheduler(1);
		rs.send(new MyMessage(1, "ID1"));
		
		assertEquals(rs.getQueue().size(), 1);
	}
	
	@Test
	public void testSingleResource2messages() {
		ResourceScheduler rs = new ResourceScheduler(1);
		rs.send(new MyMessage(1, "ID1"));
		rs.send(new MyMessage(1, "ID2"));
		
		List<Message> messagesToSend1 = rs.getMessagesToSend();
		assertEquals(((MyMessage)messagesToSend1.get(0)).getCode(), "ID1");
		
		List<Message> messagesToSend2 = rs.getMessagesToSend();
		assertEquals(((MyMessage)messagesToSend2.get(0)).getCode(), "ID2");
	}
	
	@Test
	public void testDoubleResource2messages() {
		ResourceScheduler rs = new ResourceScheduler(2);
		rs.send(new MyMessage(1, "ID1"));
		rs.send(new MyMessage(1, "ID2"));
		
		List<Message> messagesToSend = rs.getMessagesToSend();
		assertEquals(((MyMessage)messagesToSend.get(0)).getCode(), "ID1");
		assertEquals(((MyMessage)messagesToSend.get(1)).getCode(), "ID2");
	}
	
	@Test
	public void testSingleResource3messages() {
		ResourceScheduler rs = new ResourceScheduler(1);
		rs.send(new MyMessage(1, "ID1"));
		rs.send(new MyMessage(2, "ID2"));
		rs.send(new MyMessage(1, "ID3"));
		
		List<Message> messagesToSend1 = rs.getMessagesToSend();
		assertEquals(((MyMessage)messagesToSend1.get(0)).getCode(), "ID1");
		
		List<Message> messagesToSend2 = rs.getMessagesToSend();
		assertEquals(((MyMessage)messagesToSend2.get(0)).getCode(), "ID3");
		
		List<Message> messagesToSend3 = rs.getMessagesToSend();
		assertEquals(((MyMessage)messagesToSend3.get(0)).getCode(), "ID2");
	}
	
	@Test
	public void testDoubleResource3messages() {
		ResourceScheduler rs = new ResourceScheduler(2);
		rs.send(new MyMessage(1, "ID1"));
		rs.send(new MyMessage(2, "ID2"));
		rs.send(new MyMessage(1, "ID3"));
		
		List<Message> messagesToSend1 = rs.getMessagesToSend();
		assertEquals(((MyMessage)messagesToSend1.get(0)).getCode(), "ID1");
		assertEquals(((MyMessage)messagesToSend1.get(1)).getCode(), "ID3");
		
		List<Message> messagesToSend3 = rs.getMessagesToSend();
		assertEquals(((MyMessage)messagesToSend3.get(0)).getCode(), "ID2");
	}
	
	@Test
	public void testTripleResource3messages() {
		ResourceScheduler rs = new ResourceScheduler(3);
		rs.send(new MyMessage(1, "ID1"));
		rs.send(new MyMessage(2, "ID2"));
		rs.send(new MyMessage(1, "ID3"));
		
		List<Message> messagesToSend1 = rs.getMessagesToSend();
		assertEquals(((MyMessage)messagesToSend1.get(0)).getCode(), "ID1");
		assertEquals(((MyMessage)messagesToSend1.get(1)).getCode(), "ID3");
		assertEquals(((MyMessage)messagesToSend1.get(2)).getCode(), "ID2");
	}
	
	@Test
	public void testTripleResource4messages() {
		ResourceScheduler rs = new ResourceScheduler(3);
		rs.send(new MyMessage(1, "ID1"));
		rs.send(new MyMessage(2, "ID2"));
		rs.send(new MyMessage(1, "ID3"));
		rs.send(new MyMessage(1, "ID4"));
		
		List<Message> messagesToSend1 = rs.getMessagesToSend();
		assertEquals(((MyMessage)messagesToSend1.get(0)).getCode(), "ID1");
		assertEquals(((MyMessage)messagesToSend1.get(1)).getCode(), "ID3");
		assertEquals(((MyMessage)messagesToSend1.get(2)).getCode(), "ID4");
		
		List<Message> messagesToSend2 = rs.getMessagesToSend();
		assertEquals(((MyMessage)messagesToSend2.get(0)).getCode(), "ID2");
	}
	
	@Test
	public void testTripleResource4messages3groups() {
		ResourceScheduler rs = new ResourceScheduler(3);
		rs.send(new MyMessage(1, "ID1"));
		rs.send(new MyMessage(2, "ID2"));
		rs.send(new MyMessage(3, "ID3"));
		rs.send(new MyMessage(1, "ID4"));
		
		List<Message> messagesToSend1 = rs.getMessagesToSend();
		assertEquals(((MyMessage)messagesToSend1.get(0)).getCode(), "ID1");
		assertEquals(((MyMessage)messagesToSend1.get(1)).getCode(), "ID4");
		assertEquals(((MyMessage)messagesToSend1.get(2)).getCode(), "ID2");
		
		List<Message> messagesToSend2 = rs.getMessagesToSend();
		assertEquals(((MyMessage)messagesToSend2.get(0)).getCode(), "ID3");
	}
	
	@Test
	public void testQuadResource4messages3groups() {
		ResourceScheduler rs = new ResourceScheduler(4);
		rs.send(new MyMessage(1, "ID1"));
		rs.send(new MyMessage(2, "ID2"));
		rs.send(new MyMessage(3, "ID3"));
		rs.send(new MyMessage(1, "ID4"));
		
		List<Message> messagesToSend1 = rs.getMessagesToSend();
		assertEquals(((MyMessage)messagesToSend1.get(0)).getCode(), "ID1");
		assertEquals(((MyMessage)messagesToSend1.get(1)).getCode(), "ID4");
		assertEquals(((MyMessage)messagesToSend1.get(2)).getCode(), "ID2");
		assertEquals(((MyMessage)messagesToSend1.get(3)).getCode(), "ID3");
	}

}
