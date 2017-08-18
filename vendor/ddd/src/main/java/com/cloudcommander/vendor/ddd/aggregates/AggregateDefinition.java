package com.cloudcommander.vendor.ddd.aggregates;

import com.cloudcommander.vendor.ddd.aggregates.commands.Command;
import com.cloudcommander.vendor.ddd.aggregates.commands.CommandHandler;
import com.cloudcommander.vendor.ddd.aggregates.events.Event;
import com.cloudcommander.vendor.ddd.aggregates.events.EventHandler;
import com.cloudcommander.vendor.ddd.aggregates.states.StateFactory;
import com.cloudcommander.vendor.ddd.contexts.BoundedContextDefinition;

import java.util.Map;

public interface AggregateDefinition {

    BoundedContextDefinition getBoundedContextDefinition();

    StateFactory getStateFactory();

    Map<Command, CommandHandler> getCommandHandlerMap();

    Map<Event, EventHandler> getEventHandlerMap();
}
