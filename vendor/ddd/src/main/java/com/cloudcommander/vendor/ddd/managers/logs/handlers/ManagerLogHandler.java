package com.cloudcommander.vendor.ddd.managers.logs.handlers;

import com.cloudcommander.vendor.ddd.managers.logs.ManagerLog;
import com.cloudcommander.vendor.ddd.managers.states.State;

/**
 * Created by Adrian Tello on 27/09/2017.
 */
public interface ManagerLogHandler<T extends ManagerLog, S extends State> {
    void handle(ManagerLog managerLog, S state);

    Class<S> getStateClass();

    Class<T> getLogClass();
}
