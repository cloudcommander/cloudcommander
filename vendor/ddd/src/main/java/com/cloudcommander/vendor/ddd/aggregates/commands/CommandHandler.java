package com.cloudcommander.vendor.ddd.aggregates.commands;

import com.cloudcommander.vendor.ddd.aggregates.fsmstates.FSMState;
import com.cloudcommander.vendor.ddd.aggregates.states.State;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

public interface CommandHandler<T extends Command, U extends com.cloudcommander.vendor.ddd.aggregates.events.Event, S extends State, V extends FSMState>{

    @Builder
    @EqualsAndHashCode
    @Value
    class CommandHandlerResult<U, V>{
        private V newFsmState;

        @NonNull
        private U event;
    }

    CommandHandlerResult<U, V> handle(T cmd, S state);

    Class<S> getStateClass();

    Class<T> getCommandClass();
}
