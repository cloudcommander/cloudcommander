package com.cloudcommander.vendor.ddd.managers.events.handlers;

import com.cloudcommander.vendor.ddd.aggregates.events.Event;
import com.cloudcommander.vendor.ddd.managers.logs.ManagerLog;
import com.cloudcommander.vendor.ddd.managers.states.State;

import java.util.List;

/**
 * Created by Adrian Tello on 26/09/2017.
 */
public interface StateEventHandlers <T extends Event, U extends ManagerLog, S extends State>{
    String getName();

    List<EventHandler<T, U, S>> getEventHandlers();
}
