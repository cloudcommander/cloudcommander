package com.cloudcommander.vendor.ddd.aggregates.states;

public interface StateFactory<T extends State>{
    T create();
}
