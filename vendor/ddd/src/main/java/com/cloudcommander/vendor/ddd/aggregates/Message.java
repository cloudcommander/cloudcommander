package com.cloudcommander.vendor.ddd.aggregates;

import java.io.Serializable;

public interface Message <T> extends Serializable{

    T getAggregateId();
}
