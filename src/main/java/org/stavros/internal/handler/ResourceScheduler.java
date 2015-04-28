package org.stavros.internal.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceScheduler {
	
	private Configuration configuration;
	public final Configuration getConfiguration() {
		return this.configuration;
	}
	
	private Map<PriorityGroup,List<InternalMessage>> convertIntoMap(InternalMessage...messages) {
		Map<PriorityGroup, List<InternalMessage>> messagesMap = new HashMap<>();
		
		for (InternalMessage message: messages) {
			List<InternalMessage> list = messagesMap.get(message.getPriorityGroup());
			list.add(message);
		}
		
		return messagesMap;
	}
	
	public void send(InternalMessage...messages) {
		Map<PriorityGroup, List<InternalMessage>> messagesMap = convertIntoMap(messages);
		
		send(messagesMap);
	}
	
	public void send(Map<PriorityGroup,List<InternalMessage>> messagesMap) {
		
	}

}
