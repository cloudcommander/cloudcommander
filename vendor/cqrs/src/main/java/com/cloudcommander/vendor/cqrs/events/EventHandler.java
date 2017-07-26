package com.cloudcommander.vendor.cqrs.events;

import com.cloudcommander.vendor.cqrs.aggregates.AggregateState;

import java.util.Optional;

public interface EventHandler<T extends Event, S extends AggregateState> {
    Optional<S> handle(T event, Optional<S> currentStateOptional);

    Class<T> getEventClass();
}
