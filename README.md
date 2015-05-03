# resource-scheduler

## API
The resource handler should be configurable with the number of resources that are available behind the Gateway.
The API should be simple offering a single "send" method taking a Message interface as a parameter.
The purpose of the send method is to add objects to the queue, and while adding, a forwarding thread should also be active, in order to allow users of the resource scheduler to continue sending and queuing messages.
The message objects that are handled by the resource-scheduler should implement InternalMessage interface.

### Synchronous vs Asynchronous Gateway
Assuming the gateway is synchronous, therefore the calls to Gateway.send(Message) are blocking, in order to send more than 1 messages to be processed by more than 1 resources, threads should be used. One thread for every single blocking send method call.
A thread pool has to be created though, to avoid creating always new threads. The thread pool should have a configurable fixed size same as the configurable fixed number of the remote resources.

Following the assumption of an asynchronous gateway, there should be a way for the resource scheduler to have feedback about the completion of processing for each message. The resource scheduler should maintain the number of available resources, and: 1. the beginning of processing and 2. the completed method of the messages should update that number.


## Infrastructure
### Start a thread
Considering that the "send" method of the Gateway is blocking, while the user of the resource scheduler should be able to add messages to the queue, the creation of a thread is inevitable. The user of the resource scheduler should start the thread as a normal thread. In order to stop the thread, to exit the endless loop, user s can call stop() method and an isStopped() boolean value is used within the run() callback loop.

### Message interface
As the Gateway accepts only instances of classes that implement Message, we can extend the Message interface to support extra functionality:
1. the message group (priority)
2. the message type (termination)

### Termination
The Resource Scheduler needs to remember which groups have been terminated, so, a set of terminated group IDs is maintained to check against every new message.

### Cancellation
The Resource Scheduler needs to remember which groups have been cancelled, so, a set of cancelled group IDs is maintained to check against every new message. Moreover, the already queued messages of the cancelled group need to be removed from the queue.

## Default behavior
In every iteration of the thread loop, a set of messages with as many messages as the number of the available resources is selected.
The priority model is based on messages with the same group id, and in the order of arrival (FIFO).

## Extending the behavior
Users of the API can create subclasses of AbstractResourceHandler. This way these features are available:
*queue
*thread management (start, stop, loop)
*termination (set of terminated groups)
*cancellation (set of cancelled groups)


