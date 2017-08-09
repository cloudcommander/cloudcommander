package com.cloudcommander.vendor.models.definition;

public class AttributeDefinition {
    String name;

    AttributeType type;

    public AttributeDefinition(String name, AttributeType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public AttributeType getType() {
        return type;
    }
}
