package com.cloudcommander.vendor.ddd.managers.managerevents;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.NonFinal;

import java.io.Serializable;

/**
 * Created by Adrian Tello on 27/09/2017.
 */
@EqualsAndHashCode
@Value
@NonFinal
@Builder
public class ManagerEventEnvelope<T extends ManagerEvent & Serializable> implements Serializable{
    @NonNull
    private String stateName;

    @NonNull
    private T event;
}
