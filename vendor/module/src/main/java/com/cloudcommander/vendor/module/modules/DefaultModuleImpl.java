package com.cloudcommander.vendor.module.modules;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by Adrian Tello on 09/06/2017.
 */
public class DefaultModuleImpl implements Module{

    private final String name;

    private Collection<String> requiredModuleNames;

    private final Class springConfigClass;

    public DefaultModuleImpl(final String name, Collection<String> requiredModuleNames) {
        this.name = name;
        this.requiredModuleNames = requiredModuleNames;
        this.springConfigClass = null;
    }

    public DefaultModuleImpl(final String name, Collection<String> requiredModuleNames, Class sprintConfigClass) {
        this.name = name;
        this.requiredModuleNames = requiredModuleNames;
        this.springConfigClass = sprintConfigClass;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<String> getRequiredModuleNames() {
        return requiredModuleNames;
    }

    @Override
    public Optional<Class> getSpringConfigClass() {
        return Optional.ofNullable(springConfigClass);
    }
}
