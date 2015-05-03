package org.stavros.internal.handler;

import java.util.Iterator;

import org.stavros.external.gateway.interfaces.Gateway;
import org.stavros.internal.handler.interfaces.InternalMessage;

/**
 * This class subclasses the synchronous gateway, so, it provides a method to select the next
 * synchronous message to send to the gateway.
 * 
 * @author stavros
 *
 */
public class ResourceSchedulerSynchronous extends SynchronousGateway {

	/**
	 * Constructor getting the configuration of available resources
	 * and the instance of gateway to use to send
	 * @param gateway
	 * @param resources
	 */
	ResourceSchedulerSynchronous(Gateway gateway, int resources) {
		super(gateway, resources);
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
	 * Method to get the next prioritized message to send to the synchronous gateway.
	 */
	@Override
	protected InternalMessage getNextMessage() {
		InternalMessage ret = null;
		synchronized (getQueue()) {
			Iterator<InternalMessage> iter = getQueue().iterator();
			// get the next message of the current group
			while (ret == null
					&& iter.hasNext()) {
				InternalMessage msg = iter.next();
				if (msg.getGroupId().equals(getCurrentGroup())) {
					ret = msg;
					iter.remove();
				}
			}
			if (getQueue().size() > 0
					&& ret == null) {
				ret = getQueue().remove(0);
				setCurrentGroup(ret.getGroupId());
			}
		}
		return ret;
	}

}
