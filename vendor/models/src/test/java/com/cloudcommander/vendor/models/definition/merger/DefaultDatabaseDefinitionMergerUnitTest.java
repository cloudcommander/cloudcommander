package com.cloudcommander.vendor.models.definition.merger;

import com.cloudcommander.vendor.models.definition.AttributeDefinition;
import com.cloudcommander.vendor.models.definition.AttributeType;
import com.cloudcommander.vendor.models.definition.DatabaseDefinition;
import com.cloudcommander.vendor.models.definition.ModelDefinition;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class DefaultDatabaseDefinitionMergerUnitTest {

    private DatabaseDefinitionMerger databaseDefinitionMerger = new DefaultDatabaseDefinitionMerger();

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyDefinitions(){
        databaseDefinitionMerger.merge(Collections.emptyList());
    }

    @Test
    public void testOneDefinition(){
        //Prepare
        DatabaseDefinition databaseDefinition = new DatabaseDefinition("", Collections.emptyList());

        //Test
        DatabaseDefinition mergedDatabaseDefinition = databaseDefinitionMerger.merge(Collections.singletonList(databaseDefinition));

        //Verify
        assertEquals(databaseDefinition.getName(), mergedDatabaseDefinition.getName());
        assertEquals(mergedDatabaseDefinition.getModelDefinitions().size(), databaseDefinition.getModelDefinitions().size());
    }

    @Test
    public void testMultipleMerge(){
        //Prepare
        //cms
        AttributeDefinition cmsCodeAttrDef = new AttributeDefinition("code", AttributeType.STRING);
        AttributeDefinition cmsCreatedAttrDef = new AttributeDefinition("created", AttributeType.DATE);
        List<AttributeDefinition> cmsDefinitionAttrs = Arrays.asList(cmsCodeAttrDef, cmsCreatedAttrDef);
        ModelDefinition cmsModelDefinition1 = new ModelDefinition("Slider", cmsDefinitionAttrs);

        List<ModelDefinition> cmsModelDefinitions = Arrays.asList(cmsModelDefinition1);
        DatabaseDefinition cmsDatabaseDefinition = new DatabaseDefinition("cms", cmsModelDefinitions);

        //customcms
        AttributeDefinition customcmsCodeAttrDef = new AttributeDefinition("code", AttributeType.TEXT);
        AttributeDefinition customcmsCreatedAttrDef = new AttributeDefinition("modified", AttributeType.DATE);
        List<AttributeDefinition> customcmsDefinitionAttrs = Arrays.asList(customcmsCodeAttrDef, customcmsCreatedAttrDef);
        ModelDefinition customcmsModelDefinition1 = new ModelDefinition("Slider", customcmsDefinitionAttrs);

        AttributeDefinition customcmsEanAttrDef = new AttributeDefinition("ean", AttributeType.STRING);
        AttributeDefinition customcmsNameAttrDef = new AttributeDefinition("name", AttributeType.STRING);
        List<AttributeDefinition> customcmsProductAttrs = Arrays.asList(customcmsEanAttrDef, customcmsNameAttrDef);
        ModelDefinition customcmsProductDef = new ModelDefinition("Product", customcmsProductAttrs);

        List<ModelDefinition> customcmsModelDefinitions = Arrays.asList(customcmsModelDefinition1, customcmsProductDef);
        DatabaseDefinition customcmsDatabaseDefinition = new DatabaseDefinition("cms", customcmsModelDefinitions);

        //Test
        DatabaseDefinition mergedDatabaseDefinition = databaseDefinitionMerger.merge(Arrays.asList(cmsDatabaseDefinition, customcmsDatabaseDefinition));

        //Verify
        assertEquals("cms", mergedDatabaseDefinition.getName());

        List<ModelDefinition> modelDefinitions = mergedDatabaseDefinition.getModelDefinitions();
        assertEquals(2, modelDefinitions.size());

        ModelDefinition mergedProductDef = null;
        ModelDefinition mergedSliderDef = null;
        if(modelDefinitions.get(0).getName().equals("Product")){
            mergedProductDef = modelDefinitions.get(0);
            mergedSliderDef = modelDefinitions.get(1);
        }else{
            mergedProductDef = modelDefinitions.get(1);
            mergedSliderDef = modelDefinitions.get(0);
        }

        assertEquals("Product", mergedProductDef.getName());
        assertEquals("Slider", mergedSliderDef.getName());

        List<AttributeDefinition> mergedProductAttrs = mergedProductDef.getAttributes();
        assertEquals(mergedProductAttrs.size(), 2);

        {
            AttributeDefinition eanDefinition = null;
            AttributeDefinition nameDefinition = null;

            if(mergedProductAttrs.get(0).getName().equals("ean")){
                eanDefinition = mergedProductAttrs.get(0);
                nameDefinition = mergedProductAttrs.get(1);
            }else{
                eanDefinition = mergedProductAttrs.get(1);
                nameDefinition = mergedProductAttrs.get(0);
            }

            assertEquals("ean", eanDefinition.getName());
            assertEquals("name", nameDefinition.getName());

            assertEquals(AttributeType.STRING, eanDefinition.getType());
            assertEquals(AttributeType.STRING, nameDefinition.getType());
        }

        {
            List<AttributeDefinition> mergedSliderAttrs = mergedSliderDef.getAttributes();
            assertEquals(3, mergedSliderAttrs.size());

            Map<String, AttributeDefinition> attrDefinitionMap = mergedSliderAttrs.stream().collect(Collectors.toMap(AttributeDefinition::getName, def -> def));
            AttributeDefinition codeAttrDef = attrDefinitionMap.get("code");
            AttributeDefinition modifiedAttrDef = attrDefinitionMap.get("modified");
            AttributeDefinition createdAttrDef = attrDefinitionMap.get("created");

            assertEquals("code", codeAttrDef.getName());
            assertEquals("modified", modifiedAttrDef.getName());
            assertEquals("created", createdAttrDef.getName());

            assertEquals(AttributeType.TEXT, codeAttrDef.getType());
            assertEquals(AttributeType.DATE, modifiedAttrDef.getType());
            assertEquals(AttributeType.DATE, createdAttrDef.getType());
        }

    }
}
