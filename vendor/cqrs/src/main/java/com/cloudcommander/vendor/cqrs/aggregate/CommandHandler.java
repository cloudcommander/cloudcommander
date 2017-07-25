package com.cloudcommander.vendor.cqrs.aggregate;

import io.reactivex.Single;

import java.util.List;
import java.util.Optional;

public interface CommandHandler<T,S> {
    List<Event> handle(T command, Optional<S> currentStateOptional);
}
