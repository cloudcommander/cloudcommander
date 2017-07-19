package com.cloudcommander.vendor.module.validators;

import com.cloudcommander.vendor.module.modules.Module;
import com.cloudcommander.vendor.module.validators.exceptions.ValidationFailedException;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Adrian Tello on 10/06/2017.
 */
public class DefaultModuleValidatorImpl implements ModuleValidator{
    @Override
    public void validate(Module module) throws ValidationFailedException {
        validateName(module);
        validateDependencies(module);
    }

    protected void validateName(final Module module) throws ValidationFailedException {
        String moduleName = module.getName();

        if(moduleName == null || moduleName.isEmpty()){
            throw new ValidationFailedException("The module name can't be empty or null.");
        }
    }

    protected void validateDependencies(final Module module) throws ValidationFailedException {
        Set<String> dependenciesSet = new HashSet<>(module.getRequiredModuleNames());

        String moduleName = module.getName();
        if(dependenciesSet.contains(moduleName)){
            throw new ValidationFailedException("A module [" + moduleName + "] can't declare itself as a dependency.");
        }
    }
}
