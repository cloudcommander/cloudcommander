package com.cloudcommander.vendor.cqrs.commands.bus.dispatchers;

import com.cloudcommander.vendor.cqrs.bus.dispatchers.LocalMessageBusDispatcher;
import com.cloudcommander.vendor.cqrs.commands.Command;
import com.cloudcommander.vendor.cqrs.commands.CommandBusMessage;

public interface LocalCommandBusDispatcher<T extends CommandBusMessage<? extends Command>> extends CommandBusDispatcher<T>, LocalMessageBusDispatcher<T>{
}
