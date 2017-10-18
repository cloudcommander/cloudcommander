package com.cloudcommander.vendor.ddd.managers.events.handlers;

import com.cloudcommander.vendor.ddd.aggregates.events.Event;
import com.cloudcommander.vendor.ddd.managers.managerevents.ManagerEvent;
import com.cloudcommander.vendor.ddd.managers.managerevents.ManagerEventEnvelope;
import com.cloudcommander.vendor.ddd.managers.states.State;

/**
 * Created by Adrian Tello on 23/09/2017.
 */
public interface EventHandler <T extends Event, U extends ManagerEvent, S extends State>{
    ManagerEventEnvelope<U> handle(T event, S state);

    Class<S> getStateClass();

    Class<T> getEventClass();
}
