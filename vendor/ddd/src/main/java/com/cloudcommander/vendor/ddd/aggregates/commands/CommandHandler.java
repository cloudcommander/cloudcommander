package com.cloudcommander.vendor.ddd.aggregates.commands;

import com.cloudcommander.vendor.ddd.aggregates.fsmstates.FSMState;
import com.cloudcommander.vendor.ddd.aggregates.states.State;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

public interface CommandHandler<U, C extends Command<U>, E extends com.cloudcommander.vendor.ddd.aggregates.events.Event<U>, S extends State, F extends FSMState>{

    @Builder
    @EqualsAndHashCode
    @Value
    class CommandHandlerResult<U, V>{
        private V newFsmState;

        @NonNull
        private U event;
    }

    CommandHandlerResult<E, F> handle(C cmd, S state);

    Class<S> getStateClass();

    Class<C> getCommandClass();
}
