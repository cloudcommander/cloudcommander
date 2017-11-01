package com.cloudcommander.vendor.ddd.entities.events;

import com.cloudcommander.vendor.ddd.entities.Message;
import com.cloudcommander.vendor.ddd.entities.fsmstates.FSMState;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

/**
 * Created by Adrian Tello on 26/10/2017.
 */
@EqualsAndHashCode(callSuper = true)
@Value
public class EventEnvelope<U, F extends FSMState> extends Message<U>{

    public EventEnvelope(U aggregateId, Event<U> event, F newFSMState){
        super(aggregateId);

        this.event = event;
        this.newFSMState = newFSMState;
    }

    @NonNull
    private Event<U> event;

    private F newFSMState;
}
