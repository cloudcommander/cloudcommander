package com.cloudcommander.vendor.ddd.managers.counter.state;

import com.cloudcommander.vendor.ddd.managers.ManagerDefinition;
import com.cloudcommander.vendor.ddd.managers.states.ManagerStateFactory;

/**
 * Created by Adrian Tello on 09/10/2017.
 */
public class CounterManagerStateFactory implements ManagerStateFactory<CounterManagerState>{
    @Override
    public CounterManagerState create(ManagerDefinition managerDefinition) {
        return CounterManagerState.builder()
                .stateName(managerDefinition.getStartStateName())
                .build();
    }
}
