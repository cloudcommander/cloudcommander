package com.cloudcommander.vendor.ddd.entities.events;

import com.cloudcommander.vendor.ddd.entities.fsmstates.FSMState;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

/**
 * Created by Adrian Tello on 26/10/2017.
 */
@EqualsAndHashCode
@Value
public class EventEnvelope<U, E extends Event<U>, F extends FSMState>{
    @NonNull
    private E event;

    private F newFSMState;
}
