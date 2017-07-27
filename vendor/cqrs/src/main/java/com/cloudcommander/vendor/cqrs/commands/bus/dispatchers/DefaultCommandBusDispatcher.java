package com.cloudcommander.vendor.cqrs.commands.bus.dispatchers;

import com.cloudcommander.vendor.cqrs.bus.dispatchers.DefaultMessageBusDispatcher;
import com.cloudcommander.vendor.cqrs.commands.Command;

public class DefaultCommandBusDispatcher<T extends Command> extends DefaultMessageBusDispatcher<T> implements LocalCommandBusDispatcher<T>{
    public DefaultCommandBusDispatcher(){
        super();
    }
}
