package com.cloudcommander.vendor.ddd.managers.managerlogs.handlers;

import com.cloudcommander.vendor.ddd.managers.managerlogs.ManagerEvent;
import com.cloudcommander.vendor.ddd.managers.states.State;

/**
 * Created by Adrian Tello on 27/09/2017.
 */
public interface ManagerEventHandler<T extends ManagerEvent, S extends State> {
    void handle(ManagerEvent managerEvent, S state);

    Class<S> getStateClass();

    Class<T> getEventClass();
}
