package com.cloudcommander.vendor.ddd.aggregates.commands;

import com.cloudcommander.vendor.ddd.aggregates.events.Event;
import com.cloudcommander.vendor.ddd.aggregates.states.AggregateState;

public interface CommandHandler<T extends Command, S extends AggregateState>{
    Event handle(T event, S state);

    Class<S> getStateClass();

    Class<T> getCommandClass();
}
