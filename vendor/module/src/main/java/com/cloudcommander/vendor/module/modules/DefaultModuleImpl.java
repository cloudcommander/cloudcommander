package com.cloudcommander.vendor.module.modules;

/**
 * Created by Adrian Tello on 09/06/2017.
 */
public class DefaultModuleImpl implements Module{

    private final String name;

    public DefaultModuleImpl(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
