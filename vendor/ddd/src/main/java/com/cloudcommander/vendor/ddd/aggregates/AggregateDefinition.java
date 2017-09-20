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

import java.util.List;

public interface AggregateDefinition<T extends Command, U extends Event, V extends Query, W extends Result, S extends State> {

    String getName();

    BoundedContextDefinition getBoundedContextDefinition();

    StateFactory<S> getStateFactory();

    List<? extends CommandHandler<T, U, S>> getCommandHandlers();

    List<? extends EventHandler<U, S>> getEventHandlers();

    List<? extends QueryHandler<V, W, S>> getQueryHandlers();
}
