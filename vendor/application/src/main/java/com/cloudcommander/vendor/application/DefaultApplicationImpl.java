package com.cloudcommander.vendor.application;

import com.cloudcommander.vendor.application.exceptions.ApplicationStartException;
import com.cloudcommander.vendor.module.loader.DefaultModuleLoaderImpl;
import com.cloudcommander.vendor.module.loader.ModuleLoader;
import com.cloudcommander.vendor.module.modules.Module;
import com.cloudcommander.vendor.module.sorter.DefaultModuleSorterImpl;
import com.cloudcommander.vendor.module.sorter.ModuleSorter;

import java.util.Collection;
import java.util.List;

/**
 * Created by Adrian Tello on 09/06/2017.
 */
public class DefaultApplicationImpl implements Application{

    @Override
    public void start(String[] args) throws ApplicationStartException {
        final Collection<Module> modules = getAvailableModules();
        final Collection<Module> sortedModules = sortModules(modules); //Modules sorted by loading order

        logApplicationTrailer(sortedModules);
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
