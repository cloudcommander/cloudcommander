package com.cloudcommander.vendor.ddd.aggregates.akka.actors.counter.commands;

import com.cloudcommander.vendor.ddd.aggregates.commands.Command;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;

import java.util.UUID;

@EqualsAndHashCode(callSuper=true)
@NonFinal
@Value
public class IncrementCommand extends Command<UUID> {
    @Builder
    public IncrementCommand(UUID aggregateId){
        super(aggregateId);
    }
}
