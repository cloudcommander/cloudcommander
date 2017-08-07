package com.cloudcommander.vendor.cqrs.services;

import com.cloudcommander.vendor.cqrs.commands.Command;
import com.cloudcommander.vendor.cqrs.commands.CommandBusMessage;
import com.cloudcommander.vendor.cqrs.commands.bus.dispatchers.CommandBusDispatcher;
import com.cloudcommander.vendor.cqrs.events.Event;
import com.cloudcommander.vendor.cqrs.events.EventBusMessage;
import com.cloudcommander.vendor.cqrs.services.strategies.UuidGenerationStrategy;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DefaultCQRSWriteService<T extends EventBusMessage<? extends Event>> implements CQRSWriteService<T>{

    private CommandBusDispatcher<CommandBusMessage<Command>> commandBusDispatcher;

    private UuidGenerationStrategy uuidGenerationStrategy;

    @Override
    public CommandBusMessage<Command> dispatchAndForget(Command command) {
        CommandBusMessage<Command> commandBusMessage = wrapCommand(command);
        commandBusDispatcher.dispatch(commandBusMessage);

        return commandBusMessage;
    }

    @Override
    public CommandDispatchResult<Command, T> dispatch(Command command) {
        CommandBusMessage<Command> commandBusMessage = dispatchAndForget(command);

        //TODO tello wait for events
        List<T> eventBusMessages = Collections.emptyList();

        CommandDispatchResult.CommandDispatchResultBuilder<Command, T> builder = CommandDispatchResult.builder();
        builder.eventBusMessages(eventBusMessages);
        builder.busMessage(commandBusMessage);

        return builder.build();
    }

    private CommandBusMessage<Command> wrapCommand(Command command) {
        CommandBusMessage.CommandBusMessageBuilder<Command> builder = CommandBusMessage.builder();
        builder.uuid(getUuidGenerationStrategy().generate());
        builder.command(command);
        builder.created(new Date());

        return builder.build();
    }

    protected CommandBusDispatcher<CommandBusMessage<Command>> getCommandBusDispatcher() {
        return commandBusDispatcher;
    }

    @Required
    public void setCommandBusDispatcher(CommandBusDispatcher<CommandBusMessage<Command>> commandBusDispatcher) {
        this.commandBusDispatcher = commandBusDispatcher;
    }

    protected UuidGenerationStrategy getUuidGenerationStrategy() {
        return uuidGenerationStrategy;
    }

    @Required
    public void setUuidGenerationStrategy(UuidGenerationStrategy uuidGenerationStrategy) {
        this.uuidGenerationStrategy = uuidGenerationStrategy;
    }
}
