package com.cloudcommander.vendor.cqrs.aggregates;

import com.cloudcommander.vendor.cqrs.commands.Command;
import com.cloudcommander.vendor.cqrs.commands.CommandHandler;
import com.cloudcommander.vendor.cqrs.events.EventHandler;
import lombok.NonNull;
import lombok.Value;

import java.util.Map;

@Value
public class AggregateConfig {

    @NonNull
    Class<AggregateState> aggregateStateClass;

    @NonNull
    Map<Class<Command>, CommandHandler> commandHandlerMap;

    @NonNull
    Map<Class<Command>, EventHandler> eventHandlerMap;
}
