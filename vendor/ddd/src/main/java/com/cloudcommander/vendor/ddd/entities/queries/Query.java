package com.cloudcommander.vendor.ddd.entities.queries;

import com.cloudcommander.vendor.ddd.entities.Message;
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
