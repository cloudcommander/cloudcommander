package com.cloudcommander.vendor.ddd.entities.commands;

import com.cloudcommander.vendor.ddd.entities.events.Event;
import com.cloudcommander.vendor.ddd.entities.fsmstates.FSMState;
import com.cloudcommander.vendor.ddd.entities.states.State;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

public interface CommandHandler<U, S extends State, F extends FSMState>{

    @Builder
    @EqualsAndHashCode
    @Value
    class CommandHandlerResult<U, F>{
        private F newFsmState;

        @NonNull
        private U event;
    }

    CommandHandlerResult<? extends Event<U>, F> handle(Command<U> cmd, S state);

    Class<S> getStateClass();

    Class<? extends Command<U>> getCommandClass();
}
