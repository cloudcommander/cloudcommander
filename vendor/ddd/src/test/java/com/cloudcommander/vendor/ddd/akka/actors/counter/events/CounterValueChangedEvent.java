package com.cloudcommander.vendor.ddd.akka.actors.counter.events;

import com.cloudcommander.vendor.ddd.aggregates.events.Event;

import java.util.UUID;

public class CounterValueChangedEvent implements Event {private UUID counterUuid;

    private long newValue;

    public CounterValueChangedEvent(UUID counterUuid, long newValue) {
        this.counterUuid = counterUuid;
        this.newValue = newValue;
    }

    public UUID getCounterUuid() {
        return counterUuid;
    }

    public long getNewValue() {
        return newValue;
    }
}
