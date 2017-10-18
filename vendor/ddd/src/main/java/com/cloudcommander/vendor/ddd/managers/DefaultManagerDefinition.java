package com.cloudcommander.vendor.ddd.managers;

import com.cloudcommander.vendor.ddd.contexts.BoundedContextDefinition;
import com.cloudcommander.vendor.ddd.managers.events.handlers.StateEventHandlers;
import com.cloudcommander.vendor.ddd.managers.managerevents.handlers.ManagerEventHandler;
import com.cloudcommander.vendor.ddd.managers.states.ManagerStateFactory;

import java.util.List;

/**
 * Created by Adrian Tello on 23/09/2017.
 */
public class DefaultManagerDefinition implements ManagerDefinition{

    private String name;

    private BoundedContextDefinition boundedContextDefinition;

    private String startStateName;

    private List<? extends StateEventHandlers> stateEventHandlers;

    private List<ManagerEventHandler> managerEventHandlers;

    private ManagerStateFactory managerStateFactory;

    public DefaultManagerDefinition(String name, BoundedContextDefinition boundedContextDefinition, String startStateName, List<? extends StateEventHandlers> stateEventHandlers, List<ManagerEventHandler> managerEventHandlers, ManagerStateFactory managerStateFactory) {
        this.name = name;
        this.boundedContextDefinition = boundedContextDefinition;
        this.startStateName = startStateName;
        this.stateEventHandlers = stateEventHandlers;
        this.managerEventHandlers = managerEventHandlers;
        this.managerStateFactory = managerStateFactory;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public BoundedContextDefinition getBoundedContextDefinition() {
        return boundedContextDefinition;
    }

    @Override
    public String getStartStateName() {
        return startStateName;
    }

    @Override
    public List<? extends StateEventHandlers> getStateEventHandlers() {
        return stateEventHandlers;
    }

    @Override
    public List<ManagerEventHandler> getManagerEventHandlers() {
        return managerEventHandlers;
    }

    @Override
    public ManagerStateFactory getManagerStateFactory() {
        return managerStateFactory;
    }
}
