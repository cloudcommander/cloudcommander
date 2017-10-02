package com.cloudcommander.vendor.ddd.akka.actors.counter.state;

import com.cloudcommander.vendor.ddd.aggregates.states.StateFactory;

public class CounterStateFactory implements StateFactory<CounterState> {
    @Override
    public CounterState create() {
        return ImmutableCounterState
                .builder()
                .value(0)
                .build();
    }
}
