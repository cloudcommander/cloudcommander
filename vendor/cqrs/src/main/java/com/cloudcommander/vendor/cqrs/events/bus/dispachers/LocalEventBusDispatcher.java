package com.cloudcommander.vendor.cqrs.events.bus.dispachers;

import com.cloudcommander.vendor.cqrs.bus.dispatchers.LocalMessageBusDispatcher;
import com.cloudcommander.vendor.cqrs.events.Event;

public interface LocalEventBusDispatcher<T extends Event> extends EventBusDispatcher<T>, LocalMessageBusDispatcher<T>{
}
