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
public class StateQueryHandlers<T extends Query, U extends Result, S extends State>{
    private FSMState fsmState;

    private List<? extends QueryHandler<T, U, S>> queryHandlers;
}
