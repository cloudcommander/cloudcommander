package com.cloudcommander.vendor.ddd.aggregates;

import com.cloudcommander.vendor.ddd.aggregates.commands.Command;
import com.cloudcommander.vendor.ddd.aggregates.commands.CommandHandler;
import com.cloudcommander.vendor.ddd.aggregates.events.Event;
import com.cloudcommander.vendor.ddd.aggregates.events.EventHandler;
import com.cloudcommander.vendor.ddd.aggregates.states.AggregateState;
import com.cloudcommander.vendor.ddd.aggregates.states.AggregateStateFactory;
import com.cloudcommander.vendor.ddd.contexts.BoundedContextDefinition;

import java.util.List;

public interface AggregateDefinition {

    BoundedContextDefinition getBoundedContextDefinition();

    AggregateStateFactory<AggregateState> getStateFactory();

    List<CommandHandler<Command, AggregateState>> getCommandHandlers();

    List<EventHandler<Event, AggregateState>> getEventHandlers();
}
