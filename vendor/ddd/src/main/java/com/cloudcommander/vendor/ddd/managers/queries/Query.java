package com.cloudcommander.vendor.ddd.managers.queries;

import com.cloudcommander.vendor.ddd.aggregates.Message;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;

@EqualsAndHashCode(callSuper=true)
@NonFinal
@Value
public class Query<T> extends Message<T>{
    public Query(T aggregateId){
        super(aggregateId);
    }
}
