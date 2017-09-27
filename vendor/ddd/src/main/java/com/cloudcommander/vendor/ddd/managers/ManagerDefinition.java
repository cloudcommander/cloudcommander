package com.cloudcommander.vendor.ddd.managers;

import com.cloudcommander.vendor.ddd.aggregates.events.Event;

import com.cloudcommander.vendor.ddd.contexts.BoundedContextDefinition;
import com.cloudcommander.vendor.ddd.managers.events.handlers.StateEventHandlers;
import com.cloudcommander.vendor.ddd.managers.logs.ManagerLog;
import com.cloudcommander.vendor.ddd.managers.logs.handlers.ManagerLogHandler;
import com.cloudcommander.vendor.ddd.managers.states.State;

import java.util.List;

/**
 * Created by Adrian Tello on 23/09/2017.
 */
public interface ManagerDefinition<T extends Event, U extends ManagerLog, S extends State> {
    String getName();

    BoundedContextDefinition getBoundedContextDefinition();

    String getStartStateName();

    List<? extends StateEventHandlers<T, U, S>> getStateEventHandlers();

    List<ManagerLogHandler<U, S>> getManagerLogHandlers();
}
