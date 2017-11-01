package com.cloudcommander.vendor.ddd.entities;

import com.cloudcommander.vendor.ddd.entities.commands.StateCommandHandlers;
import com.cloudcommander.vendor.ddd.entities.events.Event;
import com.cloudcommander.vendor.ddd.entities.events.EventHandler;
import com.cloudcommander.vendor.ddd.entities.fsmstates.FSMState;
import com.cloudcommander.vendor.ddd.entities.queries.StateQueryHandlers;
import com.cloudcommander.vendor.ddd.entities.states.State;
import com.cloudcommander.vendor.ddd.entities.states.StateFactory;
import com.cloudcommander.vendor.ddd.contexts.BoundedContextDefinition;
import lombok.*;

import java.util.List;

@Builder
@EqualsAndHashCode
@Value
public class EntityDefinition<U, S extends State, F extends FSMState> {

    @NonNull
    private String name;

    @NonNull
    private BoundedContextDefinition boundedContextDefinition;

    @NonNull
    private StateFactory<S> stateFactory;

    @NonNull
    @Singular
    private List<? extends StateCommandHandlers<U, S, F>> stateCommandHandlers;

    @NonNull
    @Singular
    private List<? extends EventHandler<U, ? extends Event<U>, S>> eventHandlers;

    @NonNull
    @Singular
    private List<? extends StateQueryHandlers<U, S, F>> stateQueryHandlers;

    @NonNull
    private F initialFSMState;
}
