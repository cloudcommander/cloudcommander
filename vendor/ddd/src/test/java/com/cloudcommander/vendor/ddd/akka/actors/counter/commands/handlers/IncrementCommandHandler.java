package com.cloudcommander.vendor.ddd.akka.actors.counter.commands.handlers;

import com.cloudcommander.vendor.ddd.aggregates.commands.CommandHandler;
import com.cloudcommander.vendor.ddd.aggregates.events.Event;
import com.cloudcommander.vendor.ddd.akka.actors.counter.commands.IncrementCommand;
import com.cloudcommander.vendor.ddd.akka.actors.counter.events.ValueChangedEvent;
import com.cloudcommander.vendor.ddd.akka.actors.counter.state.CounterState;

import java.util.UUID;

public class IncrementCommandHandler implements CommandHandler<IncrementCommand, CounterState> {
    @Override
    public Event handle(IncrementCommand cmd, CounterState state) {
        UUID uuid = cmd.getUuid();
        long newValue = state.getValue() + 1;

        return new ValueChangedEvent(uuid, newValue);
    }

    @Override
    public Class<CounterState> getStateClass() {
        return CounterState.class;
    }

    @Override
    public Class<IncrementCommand> getCommandClass() {
        return IncrementCommand.class;
    }
}
