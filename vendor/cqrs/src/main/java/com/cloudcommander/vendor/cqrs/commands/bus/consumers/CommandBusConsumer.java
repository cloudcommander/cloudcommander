package com.cloudcommander.vendor.cqrs.commands.bus.consumers;

import com.cloudcommander.vendor.cqrs.commands.Command;
import org.reactivestreams.Publisher;

public interface CommandBusConsumer<T extends Command> {
    Publisher<T> getPublisher();
}
