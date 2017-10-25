package com.cloudcommander.vendor.ddd.aggregates.commands;

import com.cloudcommander.vendor.ddd.aggregates.events.Event;
import com.cloudcommander.vendor.ddd.aggregates.fsmstates.FSMState;
import com.cloudcommander.vendor.ddd.aggregates.states.State;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

/**
 * Created by Adrian Tello on 18/10/2017.
 */
@EqualsAndHashCode
@Value
public class StateCommandHandlers<U, C extends Command<U>, E extends Event<U>, S extends State, F extends FSMState> {
    private F fsmState;

    private List<? extends CommandHandler<U, C, E, S, F>> commandHandlers;
}
