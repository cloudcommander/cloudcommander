package com.cloudcommander.vendor.ddd.entities;

import lombok.Value;
import lombok.experimental.NonFinal;

import java.io.Serializable;

@NonFinal
@Value
public class Message <T> implements Serializable{

    private T aggregateId;
}
