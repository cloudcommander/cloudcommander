package com.cloudcommander.vendor.ddd.aggregates.akka.actors.counter.commands.handlers;

import com.cloudcommander.vendor.ddd.aggregates.commands.CommandHandler;
import com.cloudcommander.vendor.ddd.aggregates.akka.actors.counter.commands.IncrementCommand;
import com.cloudcommander.vendor.ddd.aggregates.akka.actors.counter.events.ValueChangedEvent;
import com.cloudcommander.vendor.ddd.aggregates.akka.actors.counter.state.CounterState;

import java.util.UUID;

public class IncrementCommandHandler implements CommandHandler<IncrementCommand, ValueChangedEvent, CounterState> {
    @Override
    public ValueChangedEvent handle(IncrementCommand cmd, CounterState state) {
        UUID uuid = cmd.getAggregateId();
        long newValue = state.getValue() + 1;

        return ValueChangedEvent
                .builder()
                .aggregateId(uuid)
                .newValue(newValue)
                .build();
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
