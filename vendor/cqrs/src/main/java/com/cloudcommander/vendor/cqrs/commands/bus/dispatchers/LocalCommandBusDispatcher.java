package com.cloudcommander.vendor.cqrs.commands.bus.dispatchers;

import com.cloudcommander.vendor.cqrs.bus.dispatchers.LocalMessageBusDispatcher;
import com.cloudcommander.vendor.cqrs.commands.Command;

public interface LocalCommandBusDispatcher<T extends Command> extends CommandBusDispatcher<T>, LocalMessageBusDispatcher<T>{
}
