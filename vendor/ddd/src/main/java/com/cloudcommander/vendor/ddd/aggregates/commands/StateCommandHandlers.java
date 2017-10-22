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
public class StateCommandHandlers<T extends Command, U extends Event, S extends State> {
    private FSMState fsmState;

    private List<? extends CommandHandler<T, U, S>> commandHandlers;
}
