package com.cloudcommander.vendor.ddd.akka.actors.counter.state;

import com.cloudcommander.vendor.ddd.aggregates.states.State;

public class CounterState implements State {

    private long value = 0;

    public CounterState(){

    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
