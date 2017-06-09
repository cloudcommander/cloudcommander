package com.cloudcommander.vendor.module.loader;

import com.cloudcommander.vendor.module.modules.Module;
import com.cloudcommander.vendor.module.modules.ModuleModuleImpl;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Adrian Tello on 09/06/2017.
 */
public class DefaultModuleLoaderImplTest {

    private final DefaultModuleLoaderImpl defaultModuleLoader = new DefaultModuleLoaderImpl();

    @Test
    public void test(){
        Collection<Module> modules = defaultModuleLoader.getModules();

        assertEquals(1, modules.size());

        Module module = modules.iterator().next();
        assertTrue(module instanceof ModuleModuleImpl);
    }
}
