package com.cloudcommander.vendor.ddd.aggregates.akka.actors.counter.commands.handlers;

import com.cloudcommander.vendor.ddd.aggregates.akka.actors.counter.fsmstates.CounterFSMState;
import com.cloudcommander.vendor.ddd.aggregates.commands.CommandHandler;
import com.cloudcommander.vendor.ddd.aggregates.akka.actors.counter.commands.IncrementCommand;
import com.cloudcommander.vendor.ddd.aggregates.akka.actors.counter.events.ValueChangedEvent;
import com.cloudcommander.vendor.ddd.aggregates.akka.actors.counter.state.CounterState;

import java.util.UUID;

public class IncrementCommandHandler implements CommandHandler<IncrementCommand, ValueChangedEvent, CounterState, CounterFSMState> {

    @Override
    public CommandHandlerResult<ValueChangedEvent, CounterFSMState> handle(IncrementCommand cmd, CounterState state) {
        UUID uuid = cmd.getAggregateId();
        long newValue = state.getValue() + 1;

        ValueChangedEvent event = ValueChangedEvent
                .builder()
                .aggregateId(uuid)
                .newValue(newValue)
                .build();

        CommandHandlerResult.CommandHandlerResultBuilder<ValueChangedEvent, CounterFSMState> resultBuilder = CommandHandlerResult.builder();
        resultBuilder.event(event);
        return resultBuilder.build();
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
