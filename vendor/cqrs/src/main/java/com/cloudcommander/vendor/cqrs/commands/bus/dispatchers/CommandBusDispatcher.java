package com.cloudcommander.vendor.cqrs.commands.bus.dispatchers;

import com.cloudcommander.vendor.cqrs.bus.dispatchers.MessageBusDispatcher;
import com.cloudcommander.vendor.cqrs.commands.Command;
import com.cloudcommander.vendor.cqrs.commands.CommandBusMessage;

public interface CommandBusDispatcher <T extends CommandBusMessage<? extends Command>> extends MessageBusDispatcher<T>{

}
