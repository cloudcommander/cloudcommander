package com.cloudcommander.vendor.ddd.entities.commands;

import com.cloudcommander.vendor.ddd.entities.fsmstates.FSMState;
import com.cloudcommander.vendor.ddd.entities.states.State;
import lombok.*;

import java.util.List;

/**
 * Created by Adrian Tello on 18/10/2017.
 */
@Builder
@EqualsAndHashCode
@Value
public class StateCommandHandlers<U, S extends State, F extends FSMState> {

    @NonNull
    private F fsmState;

    @NonNull
    @Singular
    private List<? extends CommandHandler<U, S, F>> commandHandlers;
}
