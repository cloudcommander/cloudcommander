package com.cloudcommander.vendor.cqrs.commands.bus.consumers;

import com.cloudcommander.vendor.cqrs.bus.consumers.MessageBusConsumer;
import com.cloudcommander.vendor.cqrs.commands.Command;

public interface CommandBusConsumer<T extends Command> extends MessageBusConsumer<T>{

}
