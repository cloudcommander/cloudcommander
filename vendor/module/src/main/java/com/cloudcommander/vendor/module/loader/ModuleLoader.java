package com.cloudcommander.vendor.module.loader;

import com.cloudcommander.vendor.module.modules.Module;

import java.util.Collection;

/**
 * Created by Adrian Tello on 09/06/2017.
 */
public interface ModuleLoader {
    Collection<Module> getModules();
}
