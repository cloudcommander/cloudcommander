package com.cloudcommander.vendor.models.definition;

import java.util.List;

public class ModelDefinition {
    private String name;

    private List<AttributeDefinition> attributes;

    public ModelDefinition(String name, List<AttributeDefinition> attributes) {
        this.name = name;
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public List<AttributeDefinition> getAttributes() {
        return attributes;
    }
}
