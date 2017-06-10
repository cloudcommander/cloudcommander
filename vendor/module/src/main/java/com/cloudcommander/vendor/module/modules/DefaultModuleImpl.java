package com.cloudcommander.vendor.module.modules;

import java.util.Collection;

/**
 * Created by Adrian Tello on 09/06/2017.
 */
public class DefaultModuleImpl implements Module{

    private final String name;

    private Collection<String> requiredModuleNames;

    public DefaultModuleImpl(final String name, Collection<String> requiredModuleNames) {
        this.name = name;
        this.requiredModuleNames = requiredModuleNames;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<String> getRequiredModuleNames() {
        return requiredModuleNames;
    }
}
