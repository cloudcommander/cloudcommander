package com.cloudcommander.vendor.ddd.contexts;

public class DefaultBoundedContextDefinition implements BoundedContextDefinition{

    private String name;

    public DefaultBoundedContextDefinition(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
