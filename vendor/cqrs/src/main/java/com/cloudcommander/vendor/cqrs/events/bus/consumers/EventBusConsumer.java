package com.cloudcommander.vendor.cqrs.events.bus.consumers;

import com.cloudcommander.vendor.cqrs.bus.consumers.MessageBusConsumer;
import com.cloudcommander.vendor.cqrs.events.Event;
import com.cloudcommander.vendor.cqrs.events.EventBusMessage;
import com.cloudcommander.vendor.cqrs.events.bus.dispachers.EventBusDispatcher;

public interface EventBusConsumer<T extends EventBusMessage<? extends Event>> extends MessageBusConsumer<T>{
}
