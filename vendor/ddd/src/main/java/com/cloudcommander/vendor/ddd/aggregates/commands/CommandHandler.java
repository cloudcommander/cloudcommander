package com.cloudcommander.vendor.ddd.aggregates.commands;

import com.cloudcommander.vendor.ddd.aggregates.events.Event;
import com.cloudcommander.vendor.ddd.aggregates.states.State;

public interface CommandHandler<T extends Command, U extends Event, S extends State>{
    U handle(T cmd, S state);

    Class<S> getStateClass();

    Class<T> getCommandClass();
}
