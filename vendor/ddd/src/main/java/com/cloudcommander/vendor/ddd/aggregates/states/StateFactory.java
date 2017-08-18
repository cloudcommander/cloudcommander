package com.cloudcommander.vendor.ddd.aggregates.states;

public interface StateFactory <T extends State>{
    public T create();
}
