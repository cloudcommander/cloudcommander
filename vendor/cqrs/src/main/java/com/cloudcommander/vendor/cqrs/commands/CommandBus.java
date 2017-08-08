package com.cloudcommander.vendor.cqrs.commands;

import com.cloudcommander.vendor.cqrs.events.Event;
import io.reactivex.Single;

import java.util.List;

public interface CommandBus {
    <T extends Command> Single<List<? extends Event>> dispatch(T command);
}
