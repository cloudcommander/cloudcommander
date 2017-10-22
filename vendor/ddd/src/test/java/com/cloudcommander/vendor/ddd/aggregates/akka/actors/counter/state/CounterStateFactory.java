package com.cloudcommander.vendor.ddd.aggregates.akka.actors.counter.state;

import com.cloudcommander.vendor.ddd.aggregates.states.StateFactory;

public class CounterStateFactory implements StateFactory<CounterState> {
    @Override
    public CounterState create() {
        return CounterState
                .builder()
                .value(0)
                .build();
    }
}