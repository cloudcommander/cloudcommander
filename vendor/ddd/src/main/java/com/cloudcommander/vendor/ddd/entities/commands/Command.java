package com.cloudcommander.vendor.ddd.entities.commands;

import com.cloudcommander.vendor.ddd.entities.Message;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;

@EqualsAndHashCode(callSuper=true)
@NonFinal
@Value
public class Command<T> extends Message<T>{

    public Command(T aggregateId){
        super(aggregateId);
    }
}
