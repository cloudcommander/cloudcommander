package com.cloudcommander.vendor.cqrs.commands.bus.dispatchers;

import com.cloudcommander.vendor.cqrs.commands.Command;
import com.cloudcommander.vendor.cqrs.events.Event;

public interface LocalCommandBusDispatcher<T extends Command, S extends Event> extends CommandBusDispatcher<T, S>{
    void addOnDispatchListener(DefaultCommandBusDispatcher.OnDispatchListener<T> onDispatchListener);

    void removeOnDispatchEventListener(DefaultCommandBusDispatcher.OnDispatchListener<T> onDispatchListener);
}
