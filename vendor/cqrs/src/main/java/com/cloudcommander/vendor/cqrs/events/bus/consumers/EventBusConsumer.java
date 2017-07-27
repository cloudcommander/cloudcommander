package com.cloudcommander.vendor.cqrs.events.bus.consumers;

import com.cloudcommander.vendor.cqrs.bus.consumers.MessageBusConsumer;
import com.cloudcommander.vendor.cqrs.events.Event;

public interface EventBusConsumer<T extends Event> extends MessageBusConsumer<T>{
}
