package com.cloudcommander.vendor.cqrs.aggregate;

import java.util.List;
import java.util.Optional;

public interface EventHandler<T, S> {
    Optional<S> handle(T event, Optional<S> currentStateOptional);
}
