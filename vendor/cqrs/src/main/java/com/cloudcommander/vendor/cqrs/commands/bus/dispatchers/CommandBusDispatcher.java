package com.cloudcommander.vendor.cqrs.commands.bus.dispatchers;

import com.cloudcommander.vendor.cqrs.commands.Command;
import com.cloudcommander.vendor.cqrs.events.Event;

import java.util.List;

public interface CommandBusDispatcher <T extends Command, S extends Event>{
    void dispatchAndForget(T command);

    List<S> dispatch(T command);
}
