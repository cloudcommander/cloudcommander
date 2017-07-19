package com.cloudcommander.vendor.module.validators;

import com.cloudcommander.vendor.module.modules.Module;
import com.cloudcommander.vendor.module.validators.exceptions.ValidationFailedException;

import java.util.*;

/**
 * Created by Adrian Tello on 10/06/2017.
 */
public class DefaultModuleCollectionValidatorImpl implements ModuleCollectionValidator{

    private ModuleValidator moduleValidator;

    public DefaultModuleCollectionValidatorImpl(final ModuleValidator moduleValidator){
        this.moduleValidator = moduleValidator;
    }

    @Override
    public void validate(final Collection<Module> moduleCollection) throws ValidationFailedException {
        validateSingleExtensions(moduleCollection);
        validateRepeatedNames(moduleCollection);
    }

    protected void validateSingleExtensions(final Collection<Module> moduleCollection) throws ValidationFailedException {
        for(Module module: moduleCollection){
            moduleValidator.validate(module);
        }
    }

    protected void validateRepeatedNames(final Collection<Module> moduleCollection) throws ValidationFailedException {
        Set<String> namesSeenSet = new HashSet<>(moduleCollection.size());

        for(Module module: moduleCollection){
            String moduleName = module.getName();

            if(namesSeenSet.contains(moduleName)){
                throw new ValidationFailedException("Module name [" + moduleName + "] found multiple times.");
            }else{
                namesSeenSet.add(moduleName);
            }
        }
    }

    protected ModuleValidator getModuleValidator() {
        return moduleValidator;
    }
}
