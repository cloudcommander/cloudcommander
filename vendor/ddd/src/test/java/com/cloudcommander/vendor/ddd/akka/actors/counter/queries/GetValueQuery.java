package com.cloudcommander.vendor.ddd.akka.actors.counter.queries;

import com.cloudcommander.vendor.ddd.aggregates.queries.Query;

import java.util.UUID;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;

@EqualsAndHashCode(callSuper=true)
@NonFinal
@Value
public class GetValueQuery extends Query<UUID>{
    @Builder
    public GetValueQuery(UUID aggregateId){
        super(aggregateId);
    }
}
