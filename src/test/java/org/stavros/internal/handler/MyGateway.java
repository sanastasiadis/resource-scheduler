package org.stavros.internal.handler;

import org.stavros.external.gateway.interfaces.Gateway;
import org.stavros.external.gateway.interfaces.Message;

public class MyGateway implements Gateway {

	@Override
	public void send(final Message message) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(((MyMessage)message).getDelayMillis());
				}
				catch(InterruptedException ie) {
					
				}
				message.completed();
			}
		});
	}

}
