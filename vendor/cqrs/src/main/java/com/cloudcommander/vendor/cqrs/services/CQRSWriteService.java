package com.cloudcommander.vendor.cqrs.services;

import com.cloudcommander.vendor.cqrs.commands.Command;
import com.cloudcommander.vendor.cqrs.commands.CommandBusMessage;
import com.cloudcommander.vendor.cqrs.events.Event;
import com.cloudcommander.vendor.cqrs.events.EventBusMessage;
import com.google.common.eventbus.EventBus;
import io.reactivex.Maybe;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Wither;

import java.util.List;

public interface CQRSWriteService<T extends EventBusMessage<? extends Event>> {
    CommandBusMessage<Command> dispatchAndForget(Command command);

    CommandDispatchResult<Command, T> dispatch(Command command);

    @Builder
    @Data
    @Wither
    class CommandDispatchResult<T extends Command, S extends EventBusMessage<? extends Event>>{
        CommandBusMessage<T> busMessage;

        List<S> eventBusMessages;
    }
}
