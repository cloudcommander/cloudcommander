package com.cloudcommander.vendor.ddd.managers;

import com.cloudcommander.vendor.ddd.aggregates.events.Event;

import com.cloudcommander.vendor.ddd.contexts.BoundedContextDefinition;
import com.cloudcommander.vendor.ddd.managers.events.handlers.StateEventHandlers;
import com.cloudcommander.vendor.ddd.managers.managerevents.ManagerEvent;
import com.cloudcommander.vendor.ddd.managers.managerevents.handlers.ManagerEventHandler;
import com.cloudcommander.vendor.ddd.managers.states.ManagerStateFactory;
import com.cloudcommander.vendor.ddd.managers.states.State;

import java.util.List;

/**
 * Created by Adrian Tello on 23/09/2017.
 */
public interface ManagerDefinition<T extends Event, U extends ManagerEvent, S extends State> {
    String getName();

    BoundedContextDefinition getBoundedContextDefinition();

    String getStartStateName();

    List<? extends StateEventHandlers<T, U, S>> getStateEventHandlers();

    List<ManagerEventHandler<U, S>> getManagerEventHandlers();

    ManagerStateFactory getManagerStateFactory();
}
