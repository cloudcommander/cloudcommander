package com.cloudcommander.vendor.cqrs.bus.dispatchers;

import com.cloudcommander.vendor.cqrs.bus.BusMessage;

public interface MessageBusDispatcher <T extends BusMessage>{
    void dispatch(T message);
}
