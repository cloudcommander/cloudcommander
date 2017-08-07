package com.cloudcommander.vendor.cqrs.commands.bus.dispatchers;

import com.cloudcommander.vendor.cqrs.bus.dispatchers.DefaultMessageBusDispatcher;
import com.cloudcommander.vendor.cqrs.commands.Command;
import com.cloudcommander.vendor.cqrs.commands.CommandBusMessage;

public class DefaultCommandBusDispatcher<T extends CommandBusMessage<? extends Command>> extends DefaultMessageBusDispatcher<T> implements LocalCommandBusDispatcher<T>{
    public DefaultCommandBusDispatcher(){
        super();
    }
}
