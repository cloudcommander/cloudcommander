package com.cloudcommander.vendor.ddd.entities.commands;

import com.cloudcommander.vendor.ddd.entities.fsmstates.FSMState;
import com.cloudcommander.vendor.ddd.entities.states.State;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

public interface CommandHandler<U, BC extends Command<U>, BE extends com.cloudcommander.vendor.ddd.entities.events.Event<U>, S extends State, F extends FSMState, C extends BC>{

    @Builder
    @EqualsAndHashCode
    @Value
    class CommandHandlerResult<U, V>{
        private V newFsmState;

        @NonNull
        private U event;
    }

    CommandHandlerResult<? extends BE, F> handle(C cmd, S state);

    Class<S> getStateClass();

    Class<C> getCommandClass();
}
