package com.cloudcommander.vendor.ddd.entities.responses;

public class UnhandledCommandResponse {
    private Class<? extends Object> commandClass;

    public UnhandledCommandResponse(Class<? extends Object> commandClass) {
        this.commandClass = commandClass;
    }

    public Class<? extends Object> getCommandClass() {
        return commandClass;
    }
}
