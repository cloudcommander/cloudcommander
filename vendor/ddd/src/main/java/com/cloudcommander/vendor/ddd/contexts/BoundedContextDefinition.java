package com.cloudcommander.vendor.ddd.contexts;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@EqualsAndHashCode
@Value
public class BoundedContextDefinition {

    @NonNull
    private String name;
}
