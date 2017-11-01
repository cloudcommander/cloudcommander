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
import lombok.*;

import java.util.List;

@Builder
@EqualsAndHashCode
@Value
public class EntityDefinition<U, BC extends Command<U>, BE extends Event<U>, BQ extends Query<U>, BR extends Result<U>, S extends State, F extends FSMState> {

    @NonNull
    private String name;

    @NonNull
    private BoundedContextDefinition boundedContextDefinition;

    @NonNull
    private StateFactory<S> stateFactory;

    @NonNull
    @Singular
    private List<? extends StateCommandHandlers<U, BC, BE , S, F>> stateCommandHandlers;

    @NonNull
    @Singular
    private List<? extends EventHandler<U, BE, S, ? extends BE>> eventHandlers;

    @NonNull
    @Singular
    private List<? extends StateQueryHandlers<U, BQ, BR, S, F>> stateQueryHandlers;

    @NonNull
    private F initialFSMState;
}
