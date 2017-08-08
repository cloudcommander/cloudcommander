package com.cloudcommander.vendor.models.definition;

import lombok.Data;
import lombok.experimental.Wither;

@Data
@Wither
public class AttributeDefinition {
    String name;

    AttributeType type;
}
