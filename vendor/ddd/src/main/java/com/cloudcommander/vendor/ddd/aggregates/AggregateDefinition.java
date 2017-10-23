package com.cloudcommander.vendor.ddd.aggregates;

import com.cloudcommander.vendor.ddd.aggregates.commands.Command;
import com.cloudcommander.vendor.ddd.aggregates.commands.StateCommandHandlers;
import com.cloudcommander.vendor.ddd.aggregates.events.Event;
import com.cloudcommander.vendor.ddd.aggregates.events.EventHandler;
import com.cloudcommander.vendor.ddd.aggregates.fsmstates.FSMState;
import com.cloudcommander.vendor.ddd.aggregates.queries.Query;
import com.cloudcommander.vendor.ddd.aggregates.queries.StateQueryHandlers;
import com.cloudcommander.vendor.ddd.aggregates.results.Result;
import com.cloudcommander.vendor.ddd.aggregates.states.State;
import com.cloudcommander.vendor.ddd.aggregates.states.StateFactory;
import com.cloudcommander.vendor.ddd.contexts.BoundedContextDefinition;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode
@Value
public class AggregateDefinition<T extends Command, U extends Event, V extends Query, W extends Result, S extends State, X extends FSMState> {

    private String name;

    private BoundedContextDefinition boundedContextDefinition;

    private StateFactory<S> stateFactory;

    private List<StateCommandHandlers<T, U , S, X>> stateCommandHandlersList;

    private Map<U, EventHandler<U, S>> eventHandlerMap;

    private List<StateQueryHandlers<V, W, S>> stateQueryHandlersList;

    private FSMState initialFSMState;
}
