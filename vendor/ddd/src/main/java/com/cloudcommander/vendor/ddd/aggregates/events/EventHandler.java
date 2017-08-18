package com.cloudcommander.vendor.ddd.aggregates.events;

public interface EventHandler <T extends Event>{
    public void handle(T event);
}
