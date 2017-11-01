package com.cloudcommander.vendor.ddd.entities.queries;

import com.cloudcommander.vendor.ddd.entities.results.Result;
import com.cloudcommander.vendor.ddd.entities.states.State;

public interface QueryHandler <U, S extends State>{
    Result<U> handle(Query<U> query, S state);

    Class<S> getStateClass();

    Class<? extends Query<U>> getQueryClass();
}
