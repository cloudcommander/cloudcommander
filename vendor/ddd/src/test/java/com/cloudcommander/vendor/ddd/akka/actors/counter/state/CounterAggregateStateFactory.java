package com.cloudcommander.vendor.ddd.akka.actors.counter.state;

import com.cloudcommander.vendor.ddd.aggregates.states.AggregateStateFactory;

public class CounterAggregateStateFactory implements AggregateStateFactory<CounterState> {
    @Override
    public CounterState create() {
        return new CounterState();
    }
}
