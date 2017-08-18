package com.cloudcommander.vendor.ddd.aggregates;

import com.cloudcommander.vendor.ddd.aggregates.commands.Command;
import com.cloudcommander.vendor.ddd.aggregates.commands.CommandHandler;
import com.cloudcommander.vendor.ddd.aggregates.events.Event;
import com.cloudcommander.vendor.ddd.aggregates.events.EventHandler;
import com.cloudcommander.vendor.ddd.aggregates.states.StateFactory;

import java.util.Collections;
import java.util.Map;

public class DefaultAggregateDefinition implements AggregateDefinition{

    private StateFactory stateFactory;

    private final Map<Command, CommandHandler> commandHandlerMap;

    private final Map<Event, EventHandler> eventHandlerMap;

    public DefaultAggregateDefinition(StateFactory stateFactory, Map<Command, CommandHandler> commandHandlerMap, Map<Event, EventHandler> eventHandlerMap) {
        this.stateFactory = stateFactory;
        this.commandHandlerMap = Collections.unmodifiableMap(commandHandlerMap);
        this.eventHandlerMap = Collections.unmodifiableMap(eventHandlerMap);
    }

    @Override
    public StateFactory getStateFactory() {
        return stateFactory;
    }

    @Override
    public Map<Command, CommandHandler> getCommandHandlerMap() {
        return commandHandlerMap;
    }

    @Override
    public Map<Event, EventHandler> getEventHandlerMap() {
        return eventHandlerMap;
    }
}
