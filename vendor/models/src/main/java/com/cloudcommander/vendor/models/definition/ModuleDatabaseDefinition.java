package com.cloudcommander.vendor.models.definition;

import java.util.List;

public class ModuleDatabaseDefinition extends DatabaseDefinition{
    String module;

    public ModuleDatabaseDefinition(String module, String name, List<ModelDefinition> modelDefinitions) {
        super(name, modelDefinitions);
        this.module = module;
    }

    public String getModule() {
        return module;
    }
}
