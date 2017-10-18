package com.cloudcommander.vendor.ddd.managers.states;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.NonFinal;

import java.io.Serializable;

/**
 * Created by Adrian Tello on 23/09/2017.
 */
@EqualsAndHashCode
@Value
@NonFinal
public abstract class State implements Serializable{
    @NonNull
    private String stateName;
}
