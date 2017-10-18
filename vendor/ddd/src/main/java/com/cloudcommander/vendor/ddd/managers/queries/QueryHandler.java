package com.cloudcommander.vendor.ddd.managers.queries;


import com.cloudcommander.vendor.ddd.managers.results.Result;
import com.cloudcommander.vendor.ddd.managers.states.State;

/**
 * Created by Adrian Tello on 10/10/2017.
 */
public interface QueryHandler<T extends Query, U extends Result, S extends State>{
    U handle(T query, S state);

    Class<S> getStateClass();

    Class<T> getQueryClass();
}