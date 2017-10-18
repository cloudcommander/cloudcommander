package com.cloudcommander.vendor.ddd.managers.results;

import com.cloudcommander.vendor.ddd.aggregates.Message;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;

/**
 * Created by adria on 10/10/2017.
 */
@EqualsAndHashCode(callSuper=true)
@NonFinal
@Value
public class Result<T> extends Message<T> {
    public Result(T aggregateId){
        super(aggregateId);
    }
}