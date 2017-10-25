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
public class AggregateDefinition<U, C extends Command<U>, E extends Event<U>, Q extends Query<U>, R extends Result<U>, S extends State, F extends FSMState> {

    private String name;

    private BoundedContextDefinition boundedContextDefinition;

    private StateFactory<S> stateFactory;

    private List<? extends StateCommandHandlers<U, C, E , S, F>> stateCommandHandlersList;

    private Map<E, ? extends EventHandler<U, E, S>> eventHandlerMap;

    private List<? extends StateQueryHandlers<U, Q, R, S, F>> stateQueryHandlersList;

    private F initialFSMState;
}
