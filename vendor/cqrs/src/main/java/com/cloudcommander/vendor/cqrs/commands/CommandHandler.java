package com.cloudcommander.vendor.cqrs.commands;

import com.cloudcommander.vendor.cqrs.events.Event;

import java.util.List;
import java.util.Optional;

public interface CommandHandler<T,S> {
    List<Event> handle(T command, Optional<S> currentStateOptional);

    Class<T> getCommandClass();
}
