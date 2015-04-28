package org.stavros.internal.handler;

import org.stavros.external.gateway.interfaces.Message;

public class MyMessage implements Message {
	
	public MyMessage(int groupId, String code, int delayMillis) {
		this.groupId = groupId;
		this.code = code;
		this.delayMillis = delayMillis;
	}
	
	public MyMessage(int groupId, String code) {
		this(groupId, code, 1000);
	}
	
	private int groupId;

	@Override
	public int getGroupId() {
		// TODO Auto-generated method stub
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

}
