package org.stavros.internal.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.stavros.internal.handler.interfaces.InternalMessage;
import org.stavros.internal.handler.interfaces.MessageType;

/**
 * Abstract class providing functionality of resource scheduler queue, termination and cancellation.
 * This runnable loops forwarding messages to the gateway.
 * Subclasses decide how the right messages are selected to be forwarded.
 * 
 * @author stavros
 *
 */
public abstract class AbstractResourceScheduler extends Thread {
	
	private final static Logger LOGGER = LogManager.getLogger(AbstractResourceScheduler.class);
	
	AbstractResourceScheduler() {
		this.queue = Collections.synchronizedList(new ArrayList<InternalMessage>());
		this.cancelledGroups = new HashSet<>();
		this.terminatedGroups = new HashSet<>();
	}
	
	private List<InternalMessage> queue;
	protected List<InternalMessage> getQueue() {
		return this.queue;
	}
	
	/**
	 * Check if the message group has been:
	 * 1. cancelled, and do not send the message
	 * 2. terminated, and throw an exception
	 * If this is a termination message then add it to the terminated message groups.
	 * Finally, call the method send() of the Gateway.
	 */
	public void send(InternalMessage message) {
		if (getCancelledGroups().contains(message.getGroupId())) {
			// this message group has been cancelled, it will not be sent to the Gateway
			LOGGER.error("A message of group ID: " + message.getGroupId() + ", has been received while this group has been canceled");
			return;
		}
		if (getTerminatedGroups().contains(message.getGroupId())) {
			// this message group has been terminated. This is an error state
			LOGGER.error("A message of group ID: " + message.getGroupId() + ", has been received while a termination message has been received for this group");
			throw new IllegalStateException("Tried to send a message of group: " + message.getGroupId() + ", while this message group is already terminated");
		}
		if (message.getMessageType().equals(MessageType.TERMINATION)) {
			// this is a termination message, add this group to the set of terminated group IDs
			addTerminatedGroup(message.getGroupId());
		}
		getQueue().add(message);
	}
	
	/**
	 * this is the set to hold the cancelled groups to filter-out future messages
	 */
	private Set<String> cancelledGroups;
	/**
	 * Get the set of the cancelled group IDs
	 * @return the set of the cancelled group IDs
	 */
	public Set<String> getCancelledGroups() {
		return this.cancelledGroups;
	}
	/**
	 * Add a group Id into the set of the cancelled group IDs in order to filter-out future messages
	 * and remove any already queued messages of this group
	 * @param groupId the group ID to cancel
	 */
	public void addCancelledGroup(String groupId) {
		this.cancelledGroups.add(groupId);
		synchronized (getQueue()) {
			Iterator<InternalMessage> iter = getQueue().iterator();
			while (iter.hasNext()) {
				InternalMessage msg = iter.next();
				if (msg.getGroupId().equals(groupId)) {
					iter.remove();
				}
			}
		}
	}
	
	/**
	 * this is the set to hold the terminated groups to filter-out future messages
	 */
	private Set<String> terminatedGroups;
	/**
	 * Get the set of the terminated group IDs
	 * @return the set of the terminated group IDs
	 */
	public Set<String> getTerminatedGroups() {
		return this.terminatedGroups;
	}
	/**
	 * Add a group ID into the set of the terminated group IDs.
	 * @param groupId the group ID to terminate
	 */
	private void addTerminatedGroup(String groupId) {
		this.terminatedGroups.add(groupId);
	}
	
	protected abstract void forward();

	@Override
	public void run() {
		while (true) {
			forward();
			
			try {
				Thread.sleep(100);
			}
			catch(InterruptedException ie) {
				
			}
		}
	}

}
