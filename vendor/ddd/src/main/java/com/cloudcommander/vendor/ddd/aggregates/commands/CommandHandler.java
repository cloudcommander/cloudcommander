package com.cloudcommander.vendor.ddd.aggregates.commands;

public interface CommandHandler<T extends Command> {
    public void handle(T command);
}
