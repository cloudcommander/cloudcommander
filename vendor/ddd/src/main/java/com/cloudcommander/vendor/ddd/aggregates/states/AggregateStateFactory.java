package com.cloudcommander.vendor.ddd.aggregates.states;

public interface AggregateStateFactory<T extends AggregateState>{
    public T create();
}
