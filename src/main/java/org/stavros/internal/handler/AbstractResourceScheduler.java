package org.stavros.internal.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.stavros.external.gateway.interfaces.Gateway;
import org.stavros.internal.handler.interfaces.InternalMessage;

public abstract class AbstractResourceScheduler implements Runnable {
	
	AbstractResourceScheduler(int resources) {
		this.resources = resources;
		this.queue = Collections.synchronizedList(new ArrayList<InternalMessage>());
	}
	
	private int resources;
	protected int getResources() {
		return this.resources;
	}
	
	private List<InternalMessage> queue;
	protected List<InternalMessage> getQueue() {
		return this.queue;
	}
	
	public void send(InternalMessage msg) {
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
	
	protected abstract List<InternalMessage> getMessagesToSend();
	
	private void forward() {
		for(InternalMessage message: getMessagesToSend()) {
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
