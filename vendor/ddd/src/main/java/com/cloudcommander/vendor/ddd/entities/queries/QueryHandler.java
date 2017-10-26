package com.cloudcommander.vendor.ddd.entities.queries;

import com.cloudcommander.vendor.ddd.entities.results.Result;
import com.cloudcommander.vendor.ddd.entities.states.State;

public interface QueryHandler <U, Q extends Query<U>, R extends Result<U>, S extends State>{
    R handle(Q query, S state);

    Class<S> getStateClass();

    Class<Q> getQueryClass();
}
