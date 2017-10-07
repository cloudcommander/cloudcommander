package com.cloudcommander.vendor.ddd.managers.events.handlers;

import com.cloudcommander.vendor.ddd.aggregates.events.Event;
import com.cloudcommander.vendor.ddd.managers.managerlogs.ManagerEvent;
import com.cloudcommander.vendor.ddd.managers.states.State;

import java.util.List;

/**
 * Created by Adrian Tello on 26/09/2017.
 */
public interface StateEventHandlers <T extends Event, U extends ManagerEvent, S extends State>{
    String getName();

    List<EventHandler<T, U, S>> getEventHandlers();
}
