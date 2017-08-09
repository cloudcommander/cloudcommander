package com.cloudcommander.vendor.models.definition.merger;

import com.cloudcommander.vendor.models.definition.AttributeDefinition;
import com.cloudcommander.vendor.models.definition.AttributeType;
import com.cloudcommander.vendor.models.definition.DatabaseDefinition;
import com.cloudcommander.vendor.models.definition.ModelDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultDatabaseDefinitionMerger implements DatabaseDefinitionMerger{

    @Override
    public DatabaseDefinition merge(List<DatabaseDefinition> databaseDefinitions) {
        if(databaseDefinitions == null || databaseDefinitions.isEmpty()){
            throw new IllegalArgumentException("The definition list can't be empty or null.");
        }

        DatabaseDefinition mergedDefinition = null;

        for(DatabaseDefinition databaseDefinition: databaseDefinitions){
            if(mergedDefinition == null){
                mergedDefinition = databaseDefinition;
            }else{
                mergedDefinition = merge(mergedDefinition, databaseDefinition);
            }
        }

        return mergedDefinition;
    }

    protected DatabaseDefinition merge(DatabaseDefinition baseDefinition, DatabaseDefinition databaseDefinition) {
        if(!databaseDefinition.getName().equals(baseDefinition.getName())){
            throw new IllegalArgumentException("Can't merge database definitions with different names.");
        }

        List<ModelDefinition> mergedModelDefs = new ArrayList<>();

        Map<String, ModelDefinition> modelDefinitionMap = databaseDefinition.getModelDefinitions().stream().collect(Collectors.toMap(ModelDefinition::getName, def -> def));
        for(ModelDefinition baseModelDef: baseDefinition.getModelDefinitions()){
            String modelName = baseModelDef.getName();

            ModelDefinition mergedModelDef = baseModelDef;
            if(modelDefinitionMap.containsKey(modelName)){
                mergedModelDef = merge(baseModelDef, modelDefinitionMap.get(modelName));
                modelDefinitionMap.remove(modelName);
            }

            mergedModelDefs.add(mergedModelDef);
        }

        mergedModelDefs.addAll(modelDefinitionMap.values());

        return new DatabaseDefinition(databaseDefinition.getName(), mergedModelDefs);
    }

    protected ModelDefinition merge(ModelDefinition baseDefinition, ModelDefinition definition){
        if(!baseDefinition.getName().equals(definition.getName())){
            throw new IllegalArgumentException("Can't merge model definitions with different names.");
        }

        List<AttributeDefinition> mergedAttrDefs = new ArrayList<>();

        Map<String, AttributeDefinition> attrDefinitionMap = definition.getAttributes().stream().collect(Collectors.toMap(AttributeDefinition::getName, def -> def));
        for(AttributeDefinition baseAttrDef: baseDefinition.getAttributes()){
            String attrName = baseAttrDef.getName();

            AttributeDefinition mergedAttrDef = baseAttrDef;
            if(attrDefinitionMap.containsKey(attrName)){
                mergedAttrDef = merge(baseAttrDef, attrDefinitionMap.get(attrName));
                attrDefinitionMap.remove(attrName);
            }

            mergedAttrDefs.add(mergedAttrDef);
        }

        mergedAttrDefs.addAll(attrDefinitionMap.values());

        return new ModelDefinition(definition.getName(), mergedAttrDefs);
    }

    protected AttributeDefinition merge(AttributeDefinition baseAttrDef, AttributeDefinition attrDef) {
        if(!baseAttrDef.getName().equals(attrDef.getName())){
            throw new IllegalArgumentException("Can't merge attributes with different names.");
        }

        return new AttributeDefinition(baseAttrDef.getName(), attrDef.getType());
    }
}
