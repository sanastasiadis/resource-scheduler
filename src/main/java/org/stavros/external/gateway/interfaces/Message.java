package org.stavros.external.gateway.interfaces;

/**
 * This is the interface for the message object that the {@link Gateway} accepts
 * @author ANASTASS
 *
 */
public interface Message {
	
	int getGroupId();
	
	void completed();

}
