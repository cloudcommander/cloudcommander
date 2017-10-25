package com.cloudcommander.vendor.ddd.aggregates.events;

import com.cloudcommander.vendor.ddd.aggregates.states.State;

public interface EventHandler <U, E extends Event<U>, S extends State> {
    S handle(E event, S state);

    Class<S> getStateClass();

    Class<E> getEventClass();
}
