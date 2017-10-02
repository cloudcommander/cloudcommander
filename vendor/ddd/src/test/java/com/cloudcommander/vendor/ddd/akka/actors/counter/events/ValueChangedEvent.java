package com.cloudcommander.vendor.ddd.akka.actors.counter.events;

import com.cloudcommander.vendor.ddd.aggregates.events.Event;

import java.util.UUID;
import org.immutables.value.Value;

@Value.Immutable
public interface ValueChangedEvent extends Event<UUID> {
    long getNewValue();
}
