package com.cloudcommander.vendor.cqrs.events.bus.dispachers;

import com.cloudcommander.vendor.cqrs.bus.dispatchers.MessageBusDispatcher;
import com.cloudcommander.vendor.cqrs.events.Event;

public interface EventBusDispatcher<T extends Event> extends MessageBusDispatcher<T>{
}
