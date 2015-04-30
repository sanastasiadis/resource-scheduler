package org.stavros.internal.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.stavros.external.gateway.interfaces.Message;

public class ResourceScheduler extends AbstractResourceScheduler {
	
	ResourceScheduler(int resources) {
		super(resources);
	}
	
	private int currentGroup;
	protected int getCurrentGroup() {
		return this.currentGroup;
	}
	protected void setCurrentGroup(int currentGroup) {
		this.currentGroup = currentGroup;
	}
	
	protected List<Message> getMessagesOfCurrentGroup() {
		List<Message> messages = new ArrayList<>();
		synchronized (getQueue()) {
			Iterator<Message> iter = getQueue().iterator();
			while (iter.hasNext()) {
				Message msg = iter.next();
				if (msg.getGroupId() == getCurrentGroup()) {
					messages.add(msg);
					iter.remove();
				}
				if (messages.size() == getResources()) {
					break;
				}
			}
		}
		
		return messages;
	}
	
	@Override
	protected List<Message> getMessagesToSend() {
		List<Message> messagesToSend = new ArrayList<>();
		
		while (getQueue().size() > 0
				&& messagesToSend.size() < getResources()) {
			List<Message> messagesOfCurrentGroup = getMessagesOfCurrentGroup();
			
			// if no messages from the current group have been found,
			// then change the current group to the group of the first message in the queue
			if (messagesOfCurrentGroup.size() == 0) {
				setCurrentGroup(getQueue().get(0).getGroupId());
			}
			messagesToSend.addAll(messagesOfCurrentGroup);
		}
		
		return messagesToSend;
	}

}
