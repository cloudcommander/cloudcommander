package com.cloudcommander.vendor.cqrs.commands.bus.dispatchers;

import com.cloudcommander.vendor.cqrs.bus.dispatchers.MessageBusDispatcher;
import com.cloudcommander.vendor.cqrs.commands.Command;

public interface CommandBusDispatcher <T extends Command> extends MessageBusDispatcher<T>{

}
