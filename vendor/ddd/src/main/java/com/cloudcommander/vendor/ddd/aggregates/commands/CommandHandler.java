package com.cloudcommander.vendor.ddd.aggregates.commands;

import com.cloudcommander.vendor.ddd.aggregates.events.Event;
import com.cloudcommander.vendor.ddd.aggregates.states.State;

public interface CommandHandler<T extends Command, S extends State>{
    Event handle(T cmd, S state);

    Class<S> getStateClass();

    Class<T> getCommandClass();
}
