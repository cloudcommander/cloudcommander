package com.cloudcommander.vendor.ddd.aggregates.akka.actors.counter.fsmstates;

import com.cloudcommander.vendor.ddd.aggregates.fsmstates.FSMState;

/**
 * Created by Adrian Tello on 18/10/2017.
 */
public enum CounterFSMState implements FSMState{
    COUNTING("COUNTING");

    private final String stateName;

    CounterFSMState(String stateName) {
        this.stateName = stateName;
    }

    @Override
    public String identifier() {
        return stateName;
    }
}
