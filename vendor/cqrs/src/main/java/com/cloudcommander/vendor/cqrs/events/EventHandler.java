package com.cloudcommander.vendor.cqrs.events;

import java.util.Optional;

public interface EventHandler<T, S> {
    Optional<S> handle(T event, Optional<S> currentStateOptional);

    Class<T> getEventClass();
}
