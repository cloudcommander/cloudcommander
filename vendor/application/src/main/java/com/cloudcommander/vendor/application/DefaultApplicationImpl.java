package com.cloudcommander.vendor.application;

import com.cloudcommander.vendor.application.exceptions.ApplicationStartException;
import com.cloudcommander.vendor.module.loader.DefaultModuleLoaderImpl;
import com.cloudcommander.vendor.module.loader.ModuleLoader;
import com.cloudcommander.vendor.module.modules.Module;
import com.cloudcommander.vendor.module.sorter.DefaultModuleSorterImpl;
import com.cloudcommander.vendor.module.sorter.ModuleSorter;
import com.cloudcommander.vendor.module.validators.DefaultModuleCollectionValidatorImpl;
import com.cloudcommander.vendor.module.validators.DefaultModuleValidatorImpl;
import com.cloudcommander.vendor.module.validators.ModuleCollectionValidator;
import com.cloudcommander.vendor.module.validators.ModuleValidator;
import com.cloudcommander.vendor.module.validators.exceptions.ValidationFailedException;

import java.util.Collection;
import java.util.List;

/**
 * Created by Adrian Tello on 09/06/2017.
 */
public class DefaultApplicationImpl implements Application{

    @Override
    public void start(String[] args) throws ApplicationStartException {
        final Collection<Module> modules = getAvailableModules();
        try {
            validateModules(modules);
        } catch (ValidationFailedException e) {
            throw new ApplicationStartException(e);
        }

        final Collection<Module> sortedModules = sortModules(modules); //Modules sorted by loading order

        logApplicationTrailer(sortedModules);
    }

    private void validateModules(final Collection<Module> modules) throws ValidationFailedException {
        final ModuleValidator moduleValidator = new DefaultModuleValidatorImpl();
        final ModuleCollectionValidator moduleCollectionValidator = new DefaultModuleCollectionValidatorImpl(moduleValidator);

        moduleCollectionValidator.validate(modules);
    }

    protected Collection<Module> getAvailableModules(){
        final ModuleLoader moduleLoader = new DefaultModuleLoaderImpl();
        return moduleLoader.getModules();
    }

    protected List<Module> sortModules(final Collection<Module> modules){
        final ModuleSorter moduleSorter = new DefaultModuleSorterImpl();
        return moduleSorter.sort(modules);
    }

    protected void logApplicationTrailer(final Collection<Module> sortedModules) {
        System.out.println("========================================================");
        System.out.println("Starting system:");
        System.out.println("========================================================");
        System.out.println("Active modules:");
        for(Module module: sortedModules){
            System.out.println(module.getName());
        }
    }
}
