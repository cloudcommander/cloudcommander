package com.cloudcommander.vendor.cqrs.services;

import com.cloudcommander.vendor.cqrs.commands.Command;
import com.cloudcommander.vendor.cqrs.events.Event;
import io.reactivex.Maybe;

import java.util.List;

public interface CQRSWriteService<T extends Command,S extends Event> {
    void dispatchAndForget(T command);

    Maybe<List<S>> dispatch(T command);
}
