package org.stavros.internal.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.stavros.external.gateway.interfaces.Gateway;
import org.stavros.external.gateway.interfaces.Message;

public class MyGateway implements Gateway {
	
	private final static Logger LOGGER = LogManager.getLogger(MyGateway.class);

	@Override
	public void send(Message message) {
		LOGGER.trace("this is the gateway arrival of a message");
	}

}
