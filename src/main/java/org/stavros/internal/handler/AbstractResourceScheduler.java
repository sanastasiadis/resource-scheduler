package org.stavros.internal.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.stavros.external.gateway.interfaces.Gateway;
import org.stavros.external.gateway.interfaces.Message;

public abstract class AbstractResourceScheduler implements Runnable {
	
	AbstractResourceScheduler(int resources) {
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
	
	@Inject
	private Gateway gateway;
	protected Gateway getGateway() {
		return gateway;
	}
	
	protected abstract List<Message> getMessagesToSend();
	
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
