package com.cloudcommander.vendor.ddd.aggregates.responses;

import com.cloudcommander.vendor.ddd.aggregates.commands.Command;

public class UnhandledCommandResponse {
    private Class<? extends Object> commandClass;

    public UnhandledCommandResponse(Class<? extends Object> commandClass) {
        this.commandClass = commandClass;
    }

    public Class<? extends Object> getCommandClass() {
        return commandClass;
    }
}
