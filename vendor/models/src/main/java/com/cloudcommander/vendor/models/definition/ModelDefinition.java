package com.cloudcommander.vendor.models.definition;

import lombok.Data;
import lombok.experimental.Wither;

import java.util.Set;

@Data
@Wither
public class ModelDefinition {
    String name;

    Set<AttributeDefinition> attributes;
}
