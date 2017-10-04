package com.cloudcommander.vendor.ddd.akka.actors.counter.events;

import com.cloudcommander.vendor.ddd.aggregates.events.Event;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;

import java.util.UUID;

@EqualsAndHashCode(callSuper=true)
@NonFinal
@Value
public class ValueChangedEvent extends Event<UUID> {
    private long newValue;

    @Builder
    public ValueChangedEvent(UUID aggregateId, long newValue){
        super(aggregateId);

        this.newValue = newValue;
    }
}
