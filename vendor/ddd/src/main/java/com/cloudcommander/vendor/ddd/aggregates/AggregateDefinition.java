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
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;

import java.util.List;

@EqualsAndHashCode
@NonFinal
@Value
public class AggregateDefinition<T extends Command, U extends Event, V extends Query, W extends Result, S extends State> {

    private String name;

    private BoundedContextDefinition boundedContextDefinition;

    private StateFactory<S> stateFactory;

    private List<? extends CommandHandler<T, U, S>> commandHandlers;

    private List<? extends EventHandler<U, S>> eventHandlers;

    private List<? extends QueryHandler<V, W, S>> queryHandlers;
}
