package com.cloudcommander.vendor.ddd.managers.counter.state;

import com.cloudcommander.vendor.ddd.managers.states.State;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;

/**
 * Created by Adrian Tello on 09/10/2017.
 */

@EqualsAndHashCode(callSuper = true)
@NonFinal
@Value
public class CounterManagerState extends State{
    private long count = 0;

    @Builder
    public CounterManagerState(String stateName){
        super(stateName);
    }
}
