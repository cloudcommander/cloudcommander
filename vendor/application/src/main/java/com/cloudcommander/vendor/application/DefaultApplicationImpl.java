package com.cloudcommander.vendor.application;

import com.cloudcommander.vendor.application.exceptions.ApplicationStartException;
import com.cloudcommander.vendor.module.loader.DefaultModuleLoaderImpl;
import com.cloudcommander.vendor.module.loader.ModuleLoader;
import com.cloudcommander.vendor.module.modules.Module;

import java.util.Collection;

/**
 * Created by Adrian Tello on 09/06/2017.
 */
public class DefaultApplicationImpl implements Application{

    private ModuleLoader moduleLoader = new DefaultModuleLoaderImpl();

    @Override
    public void start(String[] args) throws ApplicationStartException {
        Collection<Module> modules = moduleLoader.getModules();

        System.out.println("========================================================");
        System.out.println("Starting system:");
        System.out.println("========================================================");
        System.out.println("Active modules:");
        for(Module module: modules){
            System.out.println(module.getName());
        }

    }
}
