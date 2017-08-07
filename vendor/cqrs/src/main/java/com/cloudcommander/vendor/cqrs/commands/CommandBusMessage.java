package com.cloudcommander.vendor.cqrs.commands;

import com.cloudcommander.vendor.cqrs.bus.BusMessage;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Wither;

import java.util.Date;
import java.util.UUID;

@Builder
@Data
@Wither
public class CommandBusMessage<T extends Command> extends BusMessage{
    UUID uuid;
    T command;
    Date created;
}
