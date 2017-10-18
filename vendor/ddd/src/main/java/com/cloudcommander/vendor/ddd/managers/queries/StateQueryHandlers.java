package com.cloudcommander.vendor.ddd.managers.queries;

import com.cloudcommander.vendor.ddd.managers.results.Result;
import com.cloudcommander.vendor.ddd.managers.states.State;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.NonFinal;

import java.util.List;

/**
 * Created by Adrian Tello on 10/10/2017.
 */
@EqualsAndHashCode
@NonFinal
@Value
public class StateQueryHandlers <T extends Query, U extends Result, S extends State>{

    @NonNull
    private String stateName;

    @NonNull
    private List<QueryHandler<T, U , S>> queryHandlers;
}
