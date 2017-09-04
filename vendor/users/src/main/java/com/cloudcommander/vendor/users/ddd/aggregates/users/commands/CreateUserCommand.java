package com.cloudcommander.vendor.users.ddd.aggregates.users.commands;

import com.cloudcommander.vendor.ddd.aggregates.commands.Command;

import java.util.UUID;

public class CreateUserCommand implements Command {

    private UUID uuid;

    public CreateUserCommand(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public Object getTargetId() {
        return this.uuid;
    }
}
