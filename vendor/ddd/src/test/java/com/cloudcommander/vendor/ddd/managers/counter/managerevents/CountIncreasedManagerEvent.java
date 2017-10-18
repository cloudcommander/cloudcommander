package com.cloudcommander.vendor.ddd.managers.counter.managerevents;

import com.cloudcommander.vendor.ddd.managers.managerevents.ManagerEvent;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;

/**
 * Created by Adrian Tello on 16/10/2017.
 */
@Builder
@EqualsAndHashCode
@NonFinal
@Value
public class CountIncreasedManagerEvent implements ManagerEvent{
    private long count;
}
