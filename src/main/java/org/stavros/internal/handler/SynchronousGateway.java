package org.stavros.internal.handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.stavros.external.gateway.interfaces.Gateway;
import org.stavros.internal.handler.interfaces.InternalMessage;

/**
 * This is the synchronous gateway superclass for resource scheduler.
 * It provides the thread pool to use to send parallel messages to the gateway.
 * 
 * @author stavros
 *
 */
public abstract class SynchronousGateway extends AbstractResourceScheduler {
	
	private final static Logger LOGGER = LogManager.getLogger(SynchronousGateway.class);
	
	/**
	 * Constructor getting the instance of gateway to send messages to, and the configuration of available resources.
	 * @param gateway the instance of gateway to send messages to
	 * @param resources the configuration of available resources
	 */
	SynchronousGateway(Gateway gateway, int resources) {
		this.gateway = gateway;
		this.executor = Executors.newFixedThreadPool(resources);
	}
	
	/**
	 * The executor service that holds the thread pool configured with the number of available resources
	 */
	ExecutorService executor;
	
	/**
	 * This is a reference to the provided gateway instance.
	 */
	private Gateway gateway;
	/**
	 * Getter of the gateway instance
	 * @return the gateway instance
	 */
	protected Gateway getGateway() {
		return this.gateway;
	}
	
	/**
	 * Abstract method to be implemented by the subclasses
	 * in order to provide a new way to select the next message
	 * from the queue to send to the gateway
	 * @return the selected next message to send to the gateway synchronously.
	 */
	protected abstract InternalMessage getNextMessage();
	
	/**
	 * The provided forward message to use a thread from the pool, sending the next message, blocking,
	 * and waiting for return.
	 */
	@Override
	protected void forward() {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				InternalMessage message = getNextMessage();
				if (message != null) {
					LOGGER.trace("Started processing message: " + message.getCode());
					getGateway().send(message);
					LOGGER.trace("Ended processing message: " + message.getCode());
				}
			}
		});
	}

}
