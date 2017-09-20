package com.cloudcommander.vendor.ddd.aggregates;

import com.cloudcommander.vendor.ddd.aggregates.commands.Command;
import com.cloudcommander.vendor.ddd.aggregates.commands.CommandHandler;
import com.cloudcommander.vendor.ddd.aggregates.events.Event;
import com.cloudcommander.vendor.ddd.aggregates.events.EventHandler;
import com.cloudcommander.vendor.ddd.aggregates.states.AggregateState;
import com.cloudcommander.vendor.ddd.aggregates.states.AggregateStateFactory;
import com.cloudcommander.vendor.ddd.contexts.BoundedContextDefinition;

import java.util.List;

public interface AggregateDefinition<T extends Command, S extends AggregateState> {

    String getName();

    BoundedContextDefinition getBoundedContextDefinition();

    AggregateStateFactory<S> getStateFactory();

    List<? extends CommandHandler<T, S>> getCommandHandlers();

    List<EventHandler<Event, AggregateState>> getEventHandlers();
}
