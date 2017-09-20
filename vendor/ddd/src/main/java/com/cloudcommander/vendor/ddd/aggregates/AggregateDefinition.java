package com.cloudcommander.vendor.ddd.aggregates;

import com.cloudcommander.vendor.ddd.aggregates.commands.Command;
import com.cloudcommander.vendor.ddd.aggregates.commands.CommandHandler;
import com.cloudcommander.vendor.ddd.aggregates.events.Event;
import com.cloudcommander.vendor.ddd.aggregates.events.EventHandler;
import com.cloudcommander.vendor.ddd.aggregates.states.State;
import com.cloudcommander.vendor.ddd.aggregates.states.StateFactory;
import com.cloudcommander.vendor.ddd.contexts.BoundedContextDefinition;

import java.util.List;

public interface AggregateDefinition<T extends Command, S extends State> {

    String getName();

    BoundedContextDefinition getBoundedContextDefinition();

    StateFactory<S> getStateFactory();

    List<? extends CommandHandler<T, S>> getCommandHandlers();

    List<EventHandler<Event, State>> getEventHandlers();
}
