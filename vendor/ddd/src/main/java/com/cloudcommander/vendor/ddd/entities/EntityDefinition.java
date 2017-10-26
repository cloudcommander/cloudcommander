package com.cloudcommander.vendor.ddd.entities;

import com.cloudcommander.vendor.ddd.entities.commands.Command;
import com.cloudcommander.vendor.ddd.entities.commands.StateCommandHandlers;
import com.cloudcommander.vendor.ddd.entities.events.Event;
import com.cloudcommander.vendor.ddd.entities.events.EventHandler;
import com.cloudcommander.vendor.ddd.entities.fsmstates.FSMState;
import com.cloudcommander.vendor.ddd.entities.queries.Query;
import com.cloudcommander.vendor.ddd.entities.queries.StateQueryHandlers;
import com.cloudcommander.vendor.ddd.entities.results.Result;
import com.cloudcommander.vendor.ddd.entities.states.State;
import com.cloudcommander.vendor.ddd.entities.states.StateFactory;
import com.cloudcommander.vendor.ddd.contexts.BoundedContextDefinition;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Builder
@EqualsAndHashCode
@Value
public class EntityDefinition<U, C extends Command<U>, E extends Event<U>, Q extends Query<U>, R extends Result<U>, S extends State, F extends FSMState> {

    private String name;

    private BoundedContextDefinition boundedContextDefinition;

    private StateFactory<S> stateFactory;

    private List<? extends StateCommandHandlers<U, C, E , S, F>> stateCommandHandlersList;

    private Map<E, ? extends EventHandler<U, E, S>> eventHandlerMap;

    private List<? extends StateQueryHandlers<U, Q, R, S, F>> stateQueryHandlersList;

    private F initialFSMState;
}
