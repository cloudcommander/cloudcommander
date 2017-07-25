package com.cloudcommander.vendor.cqrs.aggregate;

import lombok.NonNull;
import lombok.Value;

import java.util.List;
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
