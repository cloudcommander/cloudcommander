package com.cloudcommander.vendor.cqrs.commands.bus.consumers;

import com.cloudcommander.vendor.cqrs.bus.consumers.DefaultMessageBusConsumer;
import com.cloudcommander.vendor.cqrs.commands.Command;
import com.cloudcommander.vendor.cqrs.commands.CommandBusMessage;
import com.cloudcommander.vendor.cqrs.commands.bus.dispatchers.LocalCommandBusDispatcher;

public class DefaultCommandBusConsumer<T extends CommandBusMessage<? extends Command>> extends DefaultMessageBusConsumer<T> implements CommandBusConsumer<T>{
    public DefaultCommandBusConsumer(LocalCommandBusDispatcher<T> commandBusDispatcher) {
        super(commandBusDispatcher);
    }
}
