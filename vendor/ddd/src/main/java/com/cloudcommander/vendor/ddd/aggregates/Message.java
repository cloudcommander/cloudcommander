package com.cloudcommander.vendor.ddd.aggregates;

import lombok.Value;
import lombok.experimental.NonFinal;

import java.io.Serializable;

@NonFinal
@Value
public class Message <T> implements Serializable{

    private T aggregateId;
}
