package com.cloudcommander.vendor.ddd.entities.queries;

import com.cloudcommander.vendor.ddd.entities.fsmstates.FSMState;
import com.cloudcommander.vendor.ddd.entities.results.Result;
import com.cloudcommander.vendor.ddd.entities.states.State;
import lombok.*;

import java.util.List;

/**
 * Created by Adrian Tello on 18/10/2017.
 */
@Builder
@EqualsAndHashCode
@Value
public class StateQueryHandlers<U, BQ extends Query<U>, BR extends Result<U>, S extends State, F extends FSMState>{

    @NonNull
    private F fsmState;

    @NonNull
    @Singular
    private List<? extends QueryHandler<U, ? extends BQ, BR, S, ? extends BQ>> queryHandlers;
}
