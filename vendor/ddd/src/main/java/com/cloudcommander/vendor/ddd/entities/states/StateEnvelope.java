package com.cloudcommander.vendor.ddd.entities.states;

import com.cloudcommander.vendor.ddd.entities.fsmstates.FSMState;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Wither;

import java.io.Serializable;

/**
 * Created by Adrian Tello on 26/10/2017.
 */
@EqualsAndHashCode
@Value
@Wither
public class StateEnvelope<F extends FSMState, S extends State> implements Serializable{
    @NonNull
    private F fsmState;

    @NonNull
    private S state;
}
