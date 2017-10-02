package com.cloudcommander.vendor.ddd.akka.actors.counter.state;

import com.cloudcommander.vendor.ddd.aggregates.states.State;
import org.immutables.value.Value;

@Value.Immutable
public interface CounterState extends State {
    long getValue();
}
