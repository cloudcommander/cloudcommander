package com.cloudcommander.vendor.ddd.akka.actors.counter.commands;

import com.cloudcommander.vendor.ddd.aggregates.commands.Command;

import java.util.UUID;

public class IncrementCommand implements Command {
    private UUID uuid;

    public IncrementCommand(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public Object getTargetId() {
        return uuid;
    }

    public UUID getUuid() {
        return uuid;
    }
}
