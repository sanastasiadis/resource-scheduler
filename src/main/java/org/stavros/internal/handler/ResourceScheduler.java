package org.stavros.internal.handler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.stavros.internal.handler.interfaces.InternalMessage;
import org.stavros.internal.handler.interfaces.MessageType;

public class ResourceScheduler extends AbstractResourceScheduler {
	
	private final static Logger LOGGER = LogManager.getLogger(ResourceScheduler.class);
	
	ResourceScheduler(int resources) {
		super(resources);
		setAvailableResources(resources);
		this.cancelledGroups = new HashSet<>();
		this.terminatedGroups = new HashSet<>();
	}
	
	/**
	 * Check if the message group has been:
	 * 1. cancelled, and do not send the message
	 * 2. terminated, and throw an exception
	 * If this is a termination message then add it to the terminated message groups.
	 * Finally, call the method send() of the superclass
	 */
	@Override
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
		super.send(message);
	}
	
	/**
	 * the current group ID that has priority in sending messages to the Gateway
	 */
	private String currentGroup;
	/**
	 * Getter of the current group ID
	 * @return the current group Id
	 */
	protected String getCurrentGroup() {
		return this.currentGroup;
	}
	/**
	 * Setter of the current group ID
	 * @param currentGroup the current group ID to set
	 */
	protected void setCurrentGroup(String currentGroup) {
		this.currentGroup = currentGroup;
	}
	
	/**
	 * the number of available resources is critical for the resource handler
	 * to select the number of queued messages to send to the gateway
	 */
	private int availableResources;
	/**
	 * Get the number of available resources
	 * @return the number of available resources
	 */
	protected int getAvailableResources() {
		return this.availableResources;
	}
	/**
	 * Set the number of available resources
	 * @param availableResources the number of available resources
	 */
	protected void setAvailableResources(int availableResources) {
		this.availableResources = availableResources;
	}
	/**
	 * Allocate a resource, subtract 1 from the number of available resources
	 * @throws IllegalStateException if the available resources are already 0 or a negative number
	 */
	protected void allocateResource() {
		if (getAvailableResources() <= 0) {
			throw new IllegalStateException("Tried to allocate a resource when no resource was available");
		}
		this.availableResources--;
		LOGGER.debug("Allocated a resource. Available resources: " + getAvailableResources());
	}
	/**
	 * Release a resource, add 1 to the number of available resources
	 * @throws IllegalStateException if the available resources are
	 * already equal or more than the configured number of resources
	 */
	protected void releaseResource() {
		if (getAvailableResources() >= getResources()) {
			throw new IllegalStateException("Tried to release when more that all the resources are available");
		}
		this.availableResources++;
		LOGGER.debug("Released a resource. Available resources: " + getAvailableResources());
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
	 * Add a group Id into the set of the cancelled group IDs
	 * @param groupId the group ID to cancel
	 */
	public void addCancelledGroup(String groupId) {
		this.cancelledGroups.add(groupId);
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
	
	/**
	 * Loop through the queue of messages to discover messages of the current group ID.
	 * The size of the returned list should not exceed the number of available resources.
	 * @return a list of messages that belong to the current group.
	 */
	protected List<InternalMessage> getMessagesOfCurrentGroup() {
		List<InternalMessage> messages = new ArrayList<>();
		synchronized (getQueue()) {
			Iterator<InternalMessage> iter = getQueue().iterator();
			while (iter.hasNext()) {
				InternalMessage msg = iter.next();
				if (msg.getGroupId().equals(getCurrentGroup())) {
					messages.add(msg);
					iter.remove();
				}
				if (messages.size() == getAvailableResources()) {
					break;
				}
			}
		}
		
		return messages;
	}
	
	/**
	 * Creates a list of messages with the size of the available resources, selecting
	 * from the queue messages of the current group ID, or changing the current group ID
	 * when more available resources exist
	 */
	@Override
	protected List<InternalMessage> getMessagesToSend() {
		List<InternalMessage> messagesToSend = new ArrayList<>();
		
		while (getQueue().size() > 0
				&& messagesToSend.size() < getAvailableResources()) {
			List<InternalMessage> messagesOfCurrentGroup = getMessagesOfCurrentGroup();
			
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
