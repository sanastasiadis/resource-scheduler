# resource-scheduler
Resource Scheduler

The API should be simple offering a single "send" method taking a Message interface as a parameter.
The purpose of the send method is to add objects to the queue, and while adding, a forwarding thread should also be active.

Start a thread
Considering that the "send" method of the Gateway is blocking, while the user of the resource scheduler should be able to add
messages to the queue, the creation of a thread is inevitable. The user of the resource scheduler should start the thread as a normal thread. In order to stop the thread, to exit the endless loop, user s can call stop() method and an isStopped() boolean value is used
within the run() callback loop.

Forwarding messages
In every iteration of the thread loop, a set of messages with as many messages as the number of the available resources is selected.
The priority model is based on messages with the same group id, and in the order of arrival (FIFO).

As the Gateway accepts only instances of classes that implement Message, we can extend the Message interface to support extra functionality:
1. the message group (priority)
2. the message type (termination)

