package com.cloudcommander.vendor.ddd.aggregates;

import com.cloudcommander.vendor.ddd.aggregates.commands.Command;
import com.cloudcommander.vendor.ddd.aggregates.commands.CommandHandler;
import com.cloudcommander.vendor.ddd.aggregates.events.Event;
import com.cloudcommander.vendor.ddd.aggregates.events.EventHandler;
import com.cloudcommander.vendor.ddd.aggregates.queries.Query;
import com.cloudcommander.vendor.ddd.aggregates.queries.QueryHandler;
import com.cloudcommander.vendor.ddd.aggregates.results.Result;
import com.cloudcommander.vendor.ddd.aggregates.states.State;
import com.cloudcommander.vendor.ddd.aggregates.states.StateFactory;
import com.cloudcommander.vendor.ddd.contexts.BoundedContextDefinition;

import java.util.Collections;
import java.util.List;

public class DefaultAggregateDefinition implements AggregateDefinition{

    private String name;

    private BoundedContextDefinition boundedContextDefinition;

    private StateFactory stateFactory;

    private final List<CommandHandler<? extends Command, ? extends Event, ? extends State>> commandHandlers;

    private final List<EventHandler<? extends Event, ? extends State>> eventHandlers;

    private final List<QueryHandler<? extends Query, ? extends Result, ? extends State>> queryHandlers;

    public DefaultAggregateDefinition(String name, BoundedContextDefinition boundedContextDefinition, StateFactory stateFactory, final List<CommandHandler<? extends Command, ? extends Event, ? extends State>> commandHandlers, final List<EventHandler<? extends Event, ? extends State>> eventHandlers, final List<QueryHandler<? extends Query, ? extends Result, ? extends State>> queryHandlers) {
        this.name = name;
        this.boundedContextDefinition = boundedContextDefinition;
        this.stateFactory = stateFactory;
        this.commandHandlers = Collections.unmodifiableList(commandHandlers);
        this.eventHandlers = Collections.unmodifiableList(eventHandlers);
        this.queryHandlers = queryHandlers;
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
    public StateFactory<State> getStateFactory() {
        return stateFactory;
    }

    @Override
    public List<CommandHandler<? extends Command, ? extends Event, ? extends State>> getCommandHandlers() {
        return commandHandlers;
    }

    @Override
    public List<EventHandler<? extends Event, ? extends State>> getEventHandlers() {
        return eventHandlers;
    }

    @Override
    public List<QueryHandler<? extends Query, ? extends Result, ? extends State>> getQueryHandlers() {
        return queryHandlers;
    }
}
