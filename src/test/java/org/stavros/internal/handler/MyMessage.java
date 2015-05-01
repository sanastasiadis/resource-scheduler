package org.stavros.internal.handler;

import org.stavros.internal.handler.interfaces.InternalMessage;
import org.stavros.internal.handler.interfaces.MessageType;

public class MyMessage implements InternalMessage {
	
	public MyMessage(MessageType messageType, String groupId, String code, int delayMillis) {
		this.messageType = messageType;
		this.groupId = groupId;
		this.code = code;
		this.delayMillis = delayMillis;
	}
	
	public MyMessage(MessageType messageType, String groupId, String code) {
		this(messageType, groupId, code, 1000);
	}
	
	public MyMessage(String groupId, String code) {
		this(MessageType.DEFAULT, groupId, code, 1000);
	}
	
	private String groupId;

	@Override
	public String getGroupId() {
		return this.groupId;
	}

	private boolean completed;
	public boolean isCompleted() {
		return this.completed;
	}
	
	@Override
	public void completed() {
		this.completed = true;
	}
	
	private String code;
	public String getCode() {
		return this.code;
	}
	
	private int delayMillis;
	public int getDelayMillis() {
		return this.delayMillis;
	}

	private MessageType messageType;
	@Override
	public MessageType getMessageType() {
		return this.messageType;
	}

}
