package com.cloudcommander.vendor.ddd.aggregates.queries;

import com.cloudcommander.vendor.ddd.aggregates.results.Result;
import com.cloudcommander.vendor.ddd.aggregates.states.State;

public interface QueryHandler <T extends Query, U extends Result, S extends State>{
    U handle(T query, S state);

    Class<S> getStateClass();

    Class<T> getQueryClass();
}
