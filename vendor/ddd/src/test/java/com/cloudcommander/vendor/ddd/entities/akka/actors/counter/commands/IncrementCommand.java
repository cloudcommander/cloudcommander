package com.cloudcommander.vendor.ddd.entities.akka.actors.counter.commands;

import com.cloudcommander.vendor.ddd.entities.commands.Command;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.UUID;

@EqualsAndHashCode(callSuper=true)
@Value
public class IncrementCommand extends Command<UUID> {
    @Builder
    public IncrementCommand(UUID aggregateId){
        super(aggregateId);
    }
}
