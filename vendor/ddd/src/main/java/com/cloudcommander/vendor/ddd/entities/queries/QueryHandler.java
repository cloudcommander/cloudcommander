package com.cloudcommander.vendor.ddd.entities.queries;

import com.cloudcommander.vendor.ddd.entities.results.Result;
import com.cloudcommander.vendor.ddd.entities.states.State;

public interface QueryHandler <U, Q extends Query<U>, R extends Result<U>, S extends State, QS extends Q>{
    R handle(QS query, S state);

    Class<S> getStateClass();

    Class<QS> getQueryClass();
}
