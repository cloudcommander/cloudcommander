package com.cloudcommander.vendor.ddd.akka.actors.counter.results;

import com.cloudcommander.vendor.ddd.aggregates.results.Result;

public class ValueResult implements Result{
    private long value;

    public ValueResult(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
