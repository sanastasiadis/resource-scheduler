package org.stavros.internal.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.stavros.external.gateway.interfaces.Gateway;
import org.stavros.internal.handler.interfaces.InternalMessage;

/**
 * This class subclasses the asynchronous gateway and immediately forwards message to it.
 * Send method of the gateway is not considered blocking.
 * 
 * @author stavros
 *
 */
public class ResourceScheduler extends AsynchronousGateway {
	
	private final static Logger LOGGER = LogManager.getLogger(ResourceScheduler.class);
	
	/**
	 * Constructor getting the available resources number configuration
	 * and the instance of gateway to send the asynchronous messages to.
	 * @param gateway the instance of gateway to use to send messages to.
	 * @param resources the configuration of available resources.
	 */
	ResourceScheduler(Gateway gateway, int resources) {
		super(gateway);
		this.resources = resources;
		setAvailableResources(resources);
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
	 * the configured number of resources in full, the number of available resources should be smaller than this number
	 */
	private int resources;
	/**
	 * Get the configured number of resources
	 * @return the configured number of resources
	 */
	public int getResources() {
		return resources;
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
	public int getAvailableResources() {
		return this.availableResources;
	}
	/**
	 * Set the number of available resources
	 * @param availableResources the number of available resources
	 */
	/**
	 * Allocate a resource, subtract 1 from the number of available resources
	 * @throws IllegalStateException if the available resources are already 0 or a negative number
	 */
	public void allocateResource() {
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
	public void releaseResource() {
		if (getAvailableResources() >= getResources()) {
			throw new IllegalStateException("Tried to release when more that all the resources are available");
		}
		this.availableResources++;
		LOGGER.debug("Released a resource. Available resources: " + getAvailableResources());
	}
	public void setAvailableResources(int availableResources) {
		this.availableResources = availableResources;
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
