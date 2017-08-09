package com.cloudcommander.vendor.models.definition.merger;

import com.cloudcommander.vendor.models.definition.DatabaseDefinition;

import java.util.List;

public interface DatabaseDefinitionMerger {
    DatabaseDefinition merge(List<DatabaseDefinition> databaseDefinitions);
}
