package org.stavros.internal.handler;

import org.stavros.external.gateway.interfaces.Message;

public class InternalMessage implements Message {
	
	private PriorityGroup priorityGroup;
	public final PriorityGroup getPriorityGroup() {
		return this.priorityGroup;
	}

	@Override
	public void completed() {
		// TODO Auto-generated method stub
		
	}

}
