package com.cloudcommander.vendor.ddd.entities.results;

import com.cloudcommander.vendor.ddd.entities.Message;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;

@EqualsAndHashCode(callSuper=true)
@NonFinal
@Value
public class Result<T> extends Message<T>{
    public Result(T aggregateId){
        super(aggregateId);
    }
}
