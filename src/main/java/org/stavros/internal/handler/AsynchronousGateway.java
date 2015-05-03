package org.stavros.internal.handler;

import java.util.List;

import org.stavros.external.gateway.interfaces.Gateway;
import org.stavros.internal.handler.interfaces.InternalMessage;

/**
 * Subclass of the abstract resource scheduler.
 * Assuming the gateway send method returns immediately asynchronously, this class provides
 * the overridden forward method.
 * 
 * @author stavros
 *
 */
public abstract class AsynchronousGateway extends AbstractResourceScheduler {

	protected abstract List<InternalMessage> getMessagesToSend();
	
	/**
	 * Constructor getting the instance of the gateway to forward the messages to.
	 * @param gateway the instance of the gateway to forward the messages to.
	 */
	AsynchronousGateway(Gateway gateway) {
		this.gateway = gateway;
	}
	
	/**
	 * the instance of the gateway to send messages to.
	 */
	private Gateway gateway;
	/**
	 * Getter of the gateway instance.
	 * @return the gateway instance.
	 */
	protected Gateway getGateway() {
		return gateway;
	}
	
	/**
	 * Immediately send the selected messages to the gateway. The number of the messages are.
	 * The Gateway is asynchronous.
	 */
	@Override
	protected void forward() {
		for (InternalMessage message: getMessagesToSend()) {
			getGateway().send(message);
		}
	}

}
