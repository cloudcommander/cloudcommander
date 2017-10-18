package com.cloudcommander.vendor.ddd.contexts;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode
@Value
public class BoundedContextDefinition {
    private String name;
}
