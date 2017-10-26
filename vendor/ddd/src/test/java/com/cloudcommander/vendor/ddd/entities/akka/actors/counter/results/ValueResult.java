package com.cloudcommander.vendor.ddd.entities.akka.actors.counter.results;

import com.cloudcommander.vendor.ddd.entities.results.Result;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;

import java.util.UUID;

@EqualsAndHashCode(callSuper=true)
@NonFinal
@Value
public class ValueResult extends Result<UUID> {
    private long value;

    @Builder
    public ValueResult(UUID aggregateId, long value){
        super(aggregateId);

        this.value = value;
    }
}
