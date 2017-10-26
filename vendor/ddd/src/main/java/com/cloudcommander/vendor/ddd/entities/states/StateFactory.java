package com.cloudcommander.vendor.ddd.entities.states;

public interface StateFactory<T extends State>{
    T create();
}
