package org.stavros.internal.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;


public class SynchronousTest {
	
	@Test
	public void test() {
        ResourceSchedulerSynchronous rs = new ResourceSchedulerSynchronous(new MyGateway(), 1);
		rs.send(new MyMessage("1", "ID1"));
		rs.send(new MyMessage("1", "ID2"));
		
		assertEquals("ID1", rs.getNextMessage().getCode());
		assertEquals("ID2", rs.getNextMessage().getCode());
	}
	
	@Test
	public void testSingleResource1message() {
		ResourceSchedulerSynchronous rs = new ResourceSchedulerSynchronous(new MyGateway(), 1);
		rs.send(new MyMessage("1", "ID1"));
		
		assertEquals(rs.getQueue().size(), 1);
	}
	
	@Test
	public void testSingleResource2messages() {
		ResourceSchedulerSynchronous rs = new ResourceSchedulerSynchronous(new MyGateway(), 1);
		rs.send(new MyMessage("1", "ID1"));
		rs.send(new MyMessage("1", "ID2"));
		
		assertEquals(rs.getNextMessage().getCode(), "ID1");
		assertEquals(rs.getNextMessage().getCode(), "ID2");
	}
	
	@Test
	public void testDoubleResource2messages() {
		ResourceSchedulerSynchronous rs = new ResourceSchedulerSynchronous(new MyGateway(), 2);
		rs.send(new MyMessage("1", "ID1"));
		rs.send(new MyMessage("1", "ID2"));
		
		assertEquals(rs.getNextMessage().getCode(), "ID1");
		assertEquals(rs.getNextMessage().getCode(), "ID2");
	}
	
	@Test
	public void testSingleResource3messages() {
		ResourceSchedulerSynchronous rs = new ResourceSchedulerSynchronous(new MyGateway(), 1);
		rs.send(new MyMessage("1", "ID1"));
		rs.send(new MyMessage("2", "ID2"));
		rs.send(new MyMessage("1", "ID3"));
		
		assertEquals(rs.getNextMessage().getCode(), "ID1");
		assertEquals(rs.getNextMessage().getCode(), "ID3");
		assertEquals(rs.getNextMessage().getCode(), "ID2");
	}
	
	@Test
	public void testDoubleResource3messages() {
		ResourceSchedulerSynchronous rs = new ResourceSchedulerSynchronous(new MyGateway(), 2);
		rs.send(new MyMessage("1", "ID1"));
		rs.send(new MyMessage("2", "ID2"));
		rs.send(new MyMessage("1", "ID3"));
		
		assertEquals(rs.getNextMessage().getCode(), "ID1");
		assertEquals(rs.getNextMessage().getCode(), "ID3");
		assertEquals(rs.getNextMessage().getCode(), "ID2");
	}
	
	@Test
	public void testTripleResource3messages() {
		ResourceSchedulerSynchronous rs = new ResourceSchedulerSynchronous(new MyGateway(), 3);
		rs.send(new MyMessage("1", "ID1"));
		rs.send(new MyMessage("2", "ID2"));
		rs.send(new MyMessage("1", "ID3"));
		
		assertEquals(rs.getNextMessage().getCode(), "ID1");
		assertEquals(rs.getNextMessage().getCode(), "ID3");
		assertEquals(rs.getNextMessage().getCode(), "ID2");
	}
	
	@Test
	public void testTripleResource4messages() {
		ResourceSchedulerSynchronous rs = new ResourceSchedulerSynchronous(new MyGateway(), 3);
		rs.send(new MyMessage("1", "ID1"));
		rs.send(new MyMessage("2", "ID2"));
		rs.send(new MyMessage("1", "ID3"));
		rs.send(new MyMessage("1", "ID4"));
		
		assertEquals(rs.getNextMessage().getCode(), "ID1");
		assertEquals(rs.getNextMessage().getCode(), "ID3");
		assertEquals(rs.getNextMessage().getCode(), "ID4");
		assertEquals(rs.getNextMessage().getCode(), "ID2");
	}
	
	@Test
	public void testTripleResource4messages3groups() {
		ResourceSchedulerSynchronous rs = new ResourceSchedulerSynchronous(new MyGateway(), 3);
		rs.send(new MyMessage("1", "ID1"));
		rs.send(new MyMessage("2", "ID2"));
		rs.send(new MyMessage("3", "ID3"));
		rs.send(new MyMessage("1", "ID4"));
		
		assertEquals(rs.getNextMessage().getCode(), "ID1");
		assertEquals(rs.getNextMessage().getCode(), "ID4");
		assertEquals(rs.getNextMessage().getCode(), "ID2");
		assertEquals(rs.getNextMessage().getCode(), "ID3");
	}
	
	@Test
	public void testQuadResource4messages3groups() {
		ResourceSchedulerSynchronous rs = new ResourceSchedulerSynchronous(new MyGateway(), 4);
		rs.send(new MyMessage("1", "ID1"));
		rs.send(new MyMessage("2", "ID2"));
		rs.send(new MyMessage("3", "ID3"));
		rs.send(new MyMessage("1", "ID4"));
		
		assertEquals(rs.getNextMessage().getCode(), "ID1");
		assertEquals(rs.getNextMessage().getCode(), "ID4");
		assertEquals(rs.getNextMessage().getCode(), "ID2");
		assertEquals(rs.getNextMessage().getCode(), "ID3");
	}
	
	@Test
	public void testQuadResource0messages() {
		ResourceSchedulerSynchronous rs = new ResourceSchedulerSynchronous(new MyGateway(), 4);
		
		assertNull(rs.getNextMessage());
	}

}
