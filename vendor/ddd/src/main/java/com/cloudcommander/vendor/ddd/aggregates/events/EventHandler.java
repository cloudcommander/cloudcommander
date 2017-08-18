package com.cloudcommander.vendor.ddd.aggregates.events;

import com.cloudcommander.vendor.ddd.aggregates.states.AggregateState;

public interface EventHandler <T extends Event, S extends AggregateState> {
    void handle(T event, S state);

    Class<S> getStateClass();

    Class<T> getEventClass();
}
