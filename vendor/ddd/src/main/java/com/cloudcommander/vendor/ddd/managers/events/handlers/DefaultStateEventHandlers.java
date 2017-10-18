package com.cloudcommander.vendor.ddd.managers.events.handlers;

import com.cloudcommander.vendor.ddd.aggregates.events.Event;
import com.cloudcommander.vendor.ddd.managers.managerevents.ManagerEventEnvelope;
import com.cloudcommander.vendor.ddd.managers.states.State;

import java.util.List;

/**
 * Created by Adrian Tello on 27/09/2017.
 */
public class DefaultStateEventHandlers<T extends Event, U extends ManagerEventEnvelope, S extends State> implements StateEventHandlers<T, U, S>{

    private String name;

    private List<EventHandler<T, U, S>> eventHandlers;

    public DefaultStateEventHandlers(String name, List<EventHandler<T, U, S>> eventHandlers) {
        this.name = name;
        this.eventHandlers = eventHandlers;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<EventHandler<T, U, S>> getEventHandlers() {
        return eventHandlers;
    }
}
