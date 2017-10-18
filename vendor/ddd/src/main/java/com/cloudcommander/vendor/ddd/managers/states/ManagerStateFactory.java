package com.cloudcommander.vendor.ddd.managers.states;

import com.cloudcommander.vendor.ddd.managers.ManagerDefinition;

/**
 * Created by Adrian Tello on 09/10/2017.
 */
public interface ManagerStateFactory <T extends State> {
    T create(ManagerDefinition managerDefinition);
}
