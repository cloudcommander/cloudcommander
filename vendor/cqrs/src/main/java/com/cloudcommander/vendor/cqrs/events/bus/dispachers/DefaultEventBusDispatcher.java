package com.cloudcommander.vendor.cqrs.events.bus.dispachers;

import com.cloudcommander.vendor.cqrs.bus.dispatchers.DefaultMessageBusDispatcher;
import com.cloudcommander.vendor.cqrs.events.Event;

public class DefaultEventBusDispatcher<T extends Event> extends DefaultMessageBusDispatcher<T> implements LocalEventBusDispatcher<T>{

    public DefaultEventBusDispatcher(){
        super();
    }
}
