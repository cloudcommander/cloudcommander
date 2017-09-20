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

    private String name;

    private BoundedContextDefinition boundedContextDefinition;

    private AggregateStateFactory stateFactory;

    private final List<CommandHandler<? extends Command, ? extends AggregateState>> commandHandlers;

    private final List<EventHandler<? extends Event, ? extends AggregateState>> eventHandlers;

    public DefaultAggregateDefinition(String name, BoundedContextDefinition boundedContextDefinition, AggregateStateFactory stateFactory, final List<CommandHandler<? extends Command, ? extends AggregateState>> commandHandlers, final List<EventHandler<? extends Event, ? extends AggregateState>> eventHandlers) {
        this.name = name;
        this.boundedContextDefinition = boundedContextDefinition;
        this.stateFactory = stateFactory;
        this.commandHandlers = Collections.unmodifiableList(commandHandlers);
        this.eventHandlers = Collections.unmodifiableList(eventHandlers);
    }

    @Override
    public String getName() {
        return name;
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
    public List<CommandHandler<? extends Command, ? extends AggregateState>> getCommandHandlers() {
        return commandHandlers;
    }

    @Override
    public List<EventHandler<? extends Event, ? extends AggregateState>> getEventHandlers() {
        return eventHandlers;
    }
}
