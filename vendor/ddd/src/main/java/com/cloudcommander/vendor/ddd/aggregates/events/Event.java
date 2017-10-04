package com.cloudcommander.vendor.ddd.aggregates.events;

import com.cloudcommander.vendor.ddd.aggregates.Message;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;

@EqualsAndHashCode(callSuper=true)
@Value
@NonFinal
public class Event<T> extends Message<T> {
    public Event(T aggregateId){
        super(aggregateId);
    }
}
