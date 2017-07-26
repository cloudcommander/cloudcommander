package com.cloudcommander.vendor.cqrs.aggregates;

import com.cloudcommander.vendor.cqrs.commands.Command;
import com.cloudcommander.vendor.cqrs.commands.CommandHandler;
import com.cloudcommander.vendor.cqrs.events.EventHandler;
import lombok.NonNull;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
public class AggregateConfig {

    @NonNull
    Class<AggregateState> aggregateStateClass;

    @NonNull
    List<CommandHandler> commandHandlers;

    @NonNull
    List<EventHandler> eventHandlerMap;
}
