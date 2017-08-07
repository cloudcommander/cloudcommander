package com.cloudcommander.vendor.cqrs.events.bus.dispachers;

import com.cloudcommander.vendor.cqrs.bus.dispatchers.MessageBusDispatcher;
import com.cloudcommander.vendor.cqrs.events.Event;
import com.cloudcommander.vendor.cqrs.events.EventBusMessage;

public interface EventBusDispatcher<T extends EventBusMessage<? extends Event>> extends MessageBusDispatcher<T>{
}
