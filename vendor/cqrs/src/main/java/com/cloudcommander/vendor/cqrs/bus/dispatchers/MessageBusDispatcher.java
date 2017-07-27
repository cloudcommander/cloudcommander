package com.cloudcommander.vendor.cqrs.bus.dispatchers;

import com.cloudcommander.vendor.cqrs.Message;

public interface MessageBusDispatcher <T extends Message>{
    void dispatch(T message);
}
