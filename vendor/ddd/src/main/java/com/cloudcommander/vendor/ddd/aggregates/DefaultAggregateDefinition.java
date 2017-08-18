package com.cloudcommander.vendor.ddd.aggregates;

import com.cloudcommander.vendor.ddd.aggregates.commands.Command;
import com.cloudcommander.vendor.ddd.aggregates.commands.CommandHandler;
import com.cloudcommander.vendor.ddd.aggregates.events.Event;
import com.cloudcommander.vendor.ddd.aggregates.events.EventHandler;
import com.cloudcommander.vendor.ddd.aggregates.states.AggregateState;
import com.cloudcommander.vendor.ddd.aggregates.states.AggregateStateFactory;
import com.cloudcommander.vendor.ddd.contexts.BoundedContextDefinition;

import java.util.Collections;
import java.util.List;

public class DefaultAggregateDefinition implements AggregateDefinition{

    private BoundedContextDefinition boundedContextDefinition;

    private AggregateStateFactory stateFactory;

    private final List<CommandHandler<Command, AggregateState>> commandHandlers;

    private final List<EventHandler<Event, AggregateState>> eventHandlers;

    public DefaultAggregateDefinition(BoundedContextDefinition boundedContextDefinition, AggregateStateFactory stateFactory, final List<CommandHandler<Command, AggregateState>> commandHandlers, final List<EventHandler<Event, AggregateState>> eventHandlers) {
        this.boundedContextDefinition = boundedContextDefinition;
        this.stateFactory = stateFactory;
        this.commandHandlers = Collections.unmodifiableList(commandHandlers);
        this.eventHandlers = Collections.unmodifiableList(eventHandlers);
    }

    @Override
    public BoundedContextDefinition getBoundedContextDefinition() {
        return boundedContextDefinition;
    }

    @Override
    public AggregateStateFactory<AggregateState> getStateFactory() {
        return stateFactory;
    }

    @Override
    public List<CommandHandler<Command, AggregateState>> getCommandHandlers() {
        return commandHandlers;
    }

    @Override
    public List<EventHandler<Event, AggregateState>> getEventHandlers() {
        return eventHandlers;
    }
}
