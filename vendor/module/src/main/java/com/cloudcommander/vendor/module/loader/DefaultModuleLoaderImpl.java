package com.cloudcommander.vendor.module.loader;

import com.cloudcommander.vendor.module.modules.Module;
import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.ServiceLoader;

/**
 * Created by Adrian Tello on 09/06/2017.
 */
public class DefaultModuleLoaderImpl implements ModuleLoader{

     private Collection<Module> modules;

    public DefaultModuleLoaderImpl()
    {
        final ServiceLoader<Module> moduleServiceLoader = ServiceLoader.load(Module.class);
        modules = ImmutableList.copyOf(moduleServiceLoader);
    }

    @Override
    public Collection<Module> getModules() {
        return modules;
    }
}
