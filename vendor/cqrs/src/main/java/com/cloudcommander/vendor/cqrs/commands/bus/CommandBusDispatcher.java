package com.cloudcommander.vendor.cqrs.commands.bus;

import com.cloudcommander.vendor.cqrs.commands.Command;
import com.cloudcommander.vendor.cqrs.events.Event;

import java.util.List;

public interface CommandBusDispatcher {
    void dispatchAndForget(Command command);

    List<Event> dispatch(Command command);
}
