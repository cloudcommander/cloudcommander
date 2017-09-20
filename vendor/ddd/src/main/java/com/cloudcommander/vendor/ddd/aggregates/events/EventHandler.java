package com.cloudcommander.vendor.ddd.aggregates.events;

import com.cloudcommander.vendor.ddd.aggregates.states.State;

public interface EventHandler <T extends Event, S extends State> {
    void handle(T event, S state);

    Class<S> getStateClass();

    Class<T> getEventClass();
}
