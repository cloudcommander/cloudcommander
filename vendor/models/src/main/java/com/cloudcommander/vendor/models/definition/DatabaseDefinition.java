package com.cloudcommander.vendor.models.definition;

import lombok.Data;
import lombok.experimental.Wither;

import java.util.List;

@Data
@Wither
public class DatabaseDefinition {
    String name;
    List<ModelDefinition> modelDefinitions;
}
