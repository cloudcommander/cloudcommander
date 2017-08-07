package com.cloudcommander.vendor.cqrs.events.bus.consumers;

import com.cloudcommander.vendor.cqrs.bus.consumers.DefaultMessageBusConsumer;
import com.cloudcommander.vendor.cqrs.events.Event;
import com.cloudcommander.vendor.cqrs.events.EventBusMessage;
import com.cloudcommander.vendor.cqrs.events.bus.dispachers.LocalEventBusDispatcher;

public class DefaultEventBusConsumer<T extends EventBusMessage<? extends Event>> extends DefaultMessageBusConsumer<T> implements EventBusConsumer<T>{
    public DefaultEventBusConsumer(LocalEventBusDispatcher<T> messsageBusDispatcher) {
        super(messsageBusDispatcher);
    }
}
