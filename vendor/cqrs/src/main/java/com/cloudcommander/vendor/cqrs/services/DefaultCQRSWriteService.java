package com.cloudcommander.vendor.cqrs.services;

import com.cloudcommander.vendor.cqrs.commands.Command;
import com.cloudcommander.vendor.cqrs.commands.bus.dispatchers.CommandBusDispatcher;
import com.cloudcommander.vendor.cqrs.events.Event;
import io.reactivex.Maybe;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class DefaultCQRSWriteService<T extends Command, S extends Event> implements CQRSWriteService<T, S>{

    private CommandBusDispatcher<T> commandBusDispatcher;

    @Override
    public void dispatchAndForget(T command) {
        commandBusDispatcher.dispatch(command);
    }

    @Override
    public Maybe<List<S>> dispatch(T command) {
        commandBusDispatcher.dispatch(command);

        return null;//TODO tello
    }

    protected CommandBusDispatcher<T> getCommandBusDispatcher() {
        return commandBusDispatcher;
    }

    @Required
    public void setCommandBusDispatcher(CommandBusDispatcher<T> commandBusDispatcher) {
        this.commandBusDispatcher = commandBusDispatcher;
    }
}
