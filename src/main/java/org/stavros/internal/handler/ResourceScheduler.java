package org.stavros.internal.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.stavros.external.gateway.interfaces.Gateway;
import org.stavros.external.gateway.interfaces.Message;

public class ResourceScheduler implements Runnable {
	
	ResourceScheduler(int resources) {
		this.resources = resources;
		this.queue = Collections.synchronizedList(new ArrayList<Message>());
	}
	
	private int resources;
	protected int getResources() {
		return this.resources;
	}
	
	private List<Message> queue;
	protected List<Message> getQueue() {
		return this.queue;
	}
	
	public void send(Message msg) {
		getQueue().add(msg);
	}
	
	private boolean stopped;
	public void stop() {
		this.stopped = true;
	}
	public boolean isStopped() {
		return this.stopped;
	}
	
	private int currentGroup;
	protected int getCurrentGroup() {
		return this.currentGroup;
	}
	protected void setCurrentGroup(int currentGroup) {
		this.currentGroup = currentGroup;
	}
	
	@Inject
	private Gateway gateway;
	protected Gateway getGateway() {
		return gateway;
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
	
	protected List<Message> getMessagesToSend() {
		List<Message> messagesToSend = new ArrayList<>();
		
		while (getQueue().size() > 0
				&& messagesToSend.size() < getResources()) {
			List<Message> messagesOfCurrentGroup = getMessagesOfCurrentGroup();
			if (messagesOfCurrentGroup.size() == 0) {
				setCurrentGroup(getQueue().get(0).getGroupId());
			}
			messagesToSend.addAll(messagesOfCurrentGroup);
		}
		
		return messagesToSend;
	}
	
	private void forward() {
		for(Message message: getMessagesToSend()) {
			getGateway().send(message);
		}
	}

	@Override
	public void run() {
		while (!isStopped()) {
			forward();
			
			try {
				Thread.sleep(100);
			}
			catch(InterruptedException ie) {
				
			}
		}
	}

}
