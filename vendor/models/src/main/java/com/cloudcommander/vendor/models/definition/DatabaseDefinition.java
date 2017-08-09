package com.cloudcommander.vendor.models.definition;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DatabaseDefinition {
    private String name;
    private List<ModelDefinition> modelDefinitions;

    public DatabaseDefinition(String name, List<ModelDefinition> modelDefinitions) {
        validateModelDefinitions(modelDefinitions);

        this.name = name;
        this.modelDefinitions = modelDefinitions;
    }

    protected void validateModelDefinitions(List<ModelDefinition> modelDefinitions) {
        Set<String> modelNames = new HashSet<>(modelDefinitions.size());
        for(ModelDefinition modelDefinition: modelDefinitions){
            String modelName = modelDefinition.getName();

            if(modelNames.contains(modelName)){
                throw new IllegalArgumentException("DatabaseDefinition contains multiple definitions for the model [" + modelName +"]");
            }else{
                modelNames.add(modelName);
            }
        }

    }

    public String getName() {
        return name;
    }

    public List<ModelDefinition> getModelDefinitions() {
        return modelDefinitions;
    }
}
