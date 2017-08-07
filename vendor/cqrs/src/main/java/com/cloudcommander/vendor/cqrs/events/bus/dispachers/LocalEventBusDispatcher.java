package com.cloudcommander.vendor.cqrs.events.bus.dispachers;

import com.cloudcommander.vendor.cqrs.bus.dispatchers.LocalMessageBusDispatcher;
import com.cloudcommander.vendor.cqrs.events.Event;
import com.cloudcommander.vendor.cqrs.events.EventBusMessage;

public interface LocalEventBusDispatcher<T extends EventBusMessage<? extends Event>> extends EventBusDispatcher<T>, LocalMessageBusDispatcher<T>{
}
