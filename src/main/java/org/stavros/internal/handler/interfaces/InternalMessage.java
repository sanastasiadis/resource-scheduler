package org.stavros.internal.handler.interfaces;

import org.stavros.external.gateway.interfaces.Message;

public interface InternalMessage extends Message {
	
	String getGroupId();
	
	MessageType getMessageType();
	
	String getCode();

}
