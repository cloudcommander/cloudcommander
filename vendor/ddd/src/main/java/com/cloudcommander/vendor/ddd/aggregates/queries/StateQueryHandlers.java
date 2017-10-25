package com.cloudcommander.vendor.ddd.aggregates.queries;

import com.cloudcommander.vendor.ddd.aggregates.fsmstates.FSMState;
import com.cloudcommander.vendor.ddd.aggregates.results.Result;
import com.cloudcommander.vendor.ddd.aggregates.states.State;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

/**
 * Created by Adrian Tello on 18/10/2017.
 */
@EqualsAndHashCode
@Value
public class StateQueryHandlers<U, Q extends Query<U>, R extends Result<U>, S extends State, F extends FSMState>{
    private F fsmState;

    private List<? extends QueryHandler<U, Q, R, S>> queryHandlers;
}
