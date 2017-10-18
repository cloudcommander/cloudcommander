package com.cloudcommander.vendor.ddd.managers.managerevents.handlers;

import com.cloudcommander.vendor.ddd.managers.managerevents.ManagerEvent;
import com.cloudcommander.vendor.ddd.managers.managerevents.ManagerEventEnvelope;
import com.cloudcommander.vendor.ddd.managers.states.State;

/**
 * Created by Adrian Tello on 27/09/2017.
 */
public interface ManagerEventHandler<T extends ManagerEvent, S extends State> {
    void handle(ManagerEventEnvelope<T> eventEnvelope, S state);

    Class<S> getStateClass();

    Class<T> getEventClass();
}
