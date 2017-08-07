package com.cloudcommander.vendor.cqrs.commands.bus.consumers;

import com.cloudcommander.vendor.cqrs.bus.consumers.MessageBusConsumer;
import com.cloudcommander.vendor.cqrs.commands.Command;
import com.cloudcommander.vendor.cqrs.commands.CommandBusMessage;

public interface CommandBusConsumer<T extends CommandBusMessage> extends MessageBusConsumer<T>{

}
